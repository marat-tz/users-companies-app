package com.userscompanies.service;

import com.userscompanies.client.CompaniesClient;
import com.userscompanies.dto.CompanyDtoShortResponse;
import com.userscompanies.dto.UserDtoRequest;
import com.userscompanies.dto.UserDtoResponse;
import com.userscompanies.exception.ConflictException;
import com.userscompanies.exception.NotFoundException;
import com.userscompanies.mapper.CompanyMapper;
import com.userscompanies.mapper.UserMapper;
import com.userscompanies.model.Company;
import com.userscompanies.model.User;
import com.userscompanies.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    final CompaniesClient companiesClient;
    final UserRepository userRepository;
    final UserMapper userMapper;
    final CompanyMapper companyMapper;

    @Override
    public UserDtoResponse createUser(UserDtoRequest dto) {
        log.info("Создание пользователя");
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new ConflictException("Пользователь с указанной почтой уже существует");
        }

        // TODO: здесь мы получаем на выход код 500, если компания не существует. Должен быть 404
        Company company = companiesClient.findCompany(dto.getCompanyId()).orElseThrow(() ->
                new ConflictException("Нельзя создать пользователя с несуществующей компанией"));

        CompanyDtoShortResponse companyDtoShort = companyMapper.toShortDto(company);

        User user = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(user, companyDtoShort);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление пользователя");
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDtoResponse> findUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAll();

        List<Long> companiesIds = users
                .stream()
                .map(User::getCompanyId)
                .toList();

        List<Company> companies = companiesClient.findCompaniesByIds(companiesIds);
        List<CompanyDtoShortResponse> companiesDto = companies
                .stream()
                .map(companyMapper::toShortDto)
                .toList();

        Map<Long, CompanyDtoShortResponse> companyMap = companiesDto.stream()
                .collect(Collectors.toMap(CompanyDtoShortResponse::getId, dto -> dto));

        return users.stream()
                .map(user -> userMapper.toDto(user, companyMap.get(user.getCompanyId())))
                .toList();
    }

    @Override
    public UserDtoResponse findUserById(Long userId) {
        log.info("Получение пользователя по id");
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь " + userId + " не существует"));

        Company company = companiesClient.findCompany(user.getCompanyId()).orElseThrow(() ->
                new NotFoundException("Компания " + user.getCompanyId() + " не найдена"));

        return userMapper.toDto(user, companyMapper.toShortDto(company));
    }
}
