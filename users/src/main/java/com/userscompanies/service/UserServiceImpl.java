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
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        ResponseEntity<Company> response;

        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new ConflictException("Пользователь с указанным номером уже существует");
        }

        try {
            response = companiesClient.findCompany(dto.getCompanyId());
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Нельзя создать пользователя с несуществующей компанией");
        }

        CompanyDtoShortResponse companyDtoShort = companyMapper.toShortDto(response.getBody());

        User user = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(user, companyDtoShort);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление пользователя");
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDtoResponse> findUsers(List<Long> companyId) {
        log.info("Получение всех пользователей");
        List<User> users;

        if (companyId == null) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findAllByCompanyIdIn(companyId);
        }

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
        ResponseEntity<Company> response;

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь " + userId + " не существует"));

        try {
            response = companiesClient.findCompany(user.getCompanyId());
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Компания " + user.getCompanyId() + " не найдена");
        }

        return userMapper.toDto(user, companyMapper.toShortDto(response.getBody()));
    }
}
