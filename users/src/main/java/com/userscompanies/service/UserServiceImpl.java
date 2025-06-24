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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        checkUserExistsByPhone(dto.getPhone());

        ResponseEntity<Company> response = getCompanyById(dto.getCompanyId());
        CompanyDtoShortResponse companyDtoShort = companyMapper.toShortDto(response.getBody());

        User user = userRepository.save(userMapper.toEntity(dto));
        UserDtoResponse result = userMapper.toDto(user, companyDtoShort);

        log.info("Создан пользователь: {}", result);
        return result;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("Удалён пользователь с id = {}", userId);
    }

    @Override
    public Page<UserDtoResponse> findUsers(List<Long> companyId, Integer from, Integer size) {
        Page<User> usersPage;
        Pageable pageable = PageRequest.of(from / size, size);

        if (companyId == null) {
            usersPage = userRepository.findAll(pageable);
        } else {
            usersPage = userRepository.findAllByCompanyIdIn(pageable, companyId);
        }

        List<Long> companiesIds = usersPage.getContent()
                .stream()
                .map(User::getCompanyId)
                .toList();

        Page<Company> companies = companiesClient.findCompaniesByIds(companiesIds);
        List<CompanyDtoShortResponse> companiesDto = companies.getContent()
                .stream()
                .map(companyMapper::toShortDto)
                .toList();

        Map<Long, CompanyDtoShortResponse> companyMap = companiesDto.stream()
                .collect(Collectors.toMap(CompanyDtoShortResponse::getId, dto -> dto));

        List<UserDtoResponse> result = usersPage.getContent().stream()
                .map(user -> userMapper.toDto(user, companyMap.get(user.getCompanyId())))
                .toList();

        Page<UserDtoResponse> pageResult = new PageImpl<>(
                result,
                usersPage.getPageable(),
                usersPage.getTotalElements()
        );

        log.info("Получен список пользователей в количестве {}", result.size());
        return pageResult;
    }

    @Override
    public UserDtoResponse findUserById(Long userId) {
        User user = getUserById(userId);

        ResponseEntity<Company> response = getCompanyById(user.getCompanyId());
        UserDtoResponse result = userMapper.toDto(user, companyMapper.toShortDto(response.getBody()));

        log.info("Найден пользователь с id = {}, возвращаемый DTO: {}", userId, result);
        return result;
    }

    @Override
    public UserDtoResponse updateUserById(UserDtoRequest dto, Long userId) {
        User user = getUserById(userId);

        ResponseEntity<Company> response = getCompanyById(dto.getCompanyId());
        CompanyDtoShortResponse companyDto = companyMapper.toShortDto(response.getBody());

        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            user.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            user.setLastName(dto.getLastName());
        }

        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            user.setPhone(dto.getPhone());
        }

        User savedUser = userRepository.save(user);
        UserDtoResponse result = userMapper.toDto(savedUser, companyDto);

        log.info("Обновлён пользователь с id = {}, возвращаемый DTO: {}", userId, result);
        return result;
    }

    private ResponseEntity<Company> getCompanyById(Long companyId) {
        try {
            return companiesClient.findCompany(companyId);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Компания " + companyId + " не найдена");
        }
    }

    private void checkUserExistsByPhone(String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw new ConflictException("Пользователь с указанным номером уже существует");
        }
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь " + id + " не существует"));
    }
}
