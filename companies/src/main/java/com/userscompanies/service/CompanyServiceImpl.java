package com.userscompanies.service;

import com.userscompanies.client.UsersClient;
import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;
import com.userscompanies.dto.UserDtoResponse;
import com.userscompanies.exception.ConflictException;
import com.userscompanies.exception.NotFoundException;
import com.userscompanies.mapper.CompanyMapper;
import com.userscompanies.mapper.UserMapper;
import com.userscompanies.model.Company;
import com.userscompanies.repository.CompanyRepository;
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
public class CompanyServiceImpl implements CompanyService {

    private final UserMapper userMapper;

    final CompanyRepository companyRepository;
    final CompanyMapper companyMapper;
    final UsersClient usersClient;

    @Override
    public CompanyDtoShortResponse createCompany(CompanyDtoRequest dto) {
        log.info("Создание компании");
        if (companyRepository.existsByName(dto.getName())) {
            throw new ConflictException("Компания с указанным названием уже существует");
        }

        Company company = companyRepository.save(companyMapper.toEntity(dto));
        return companyMapper.toShortDto(company);
    }

    @Override
    public void deleteCompany(Long companyId) {
        log.info("Удаление компании");
        companyRepository.deleteById(companyId);
    }

    @Override
    public List<CompanyDtoFullResponse> findCompanies() {
        log.info("Получение всех компаний");
        List<Company> companies = companyRepository.findAll();

        List<Long> companiesIds = companies
                .stream()
                .map(Company::getId)
                .toList();

        List<UserDtoResponse> users = usersClient.findUsersByCompanyIds(companiesIds);
        Map<Long, List<UserDtoResponse>> userMap = users.stream()
                .collect(Collectors.groupingBy(user -> user.getCompany().getId()));

        return companies.stream()
                .map(company -> companyMapper.toDto(company, userMap.get(company.getId())))
                .toList();
    }

    @Override
    public CompanyDtoFullResponse findCompanyById(Long companyId) {
        log.info("Поиск компании по id");

        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new NotFoundException("Компания " + companyId + " не существует"));

        List<UserDtoResponse> users = usersClient.findUsersByCompanyIds(List.of(companyId));

        return companyMapper.toDto(company, users);
    }

    @Override
    public List<CompanyDtoShortResponse> findCompaniesByIds(List<Long> ids) {
        List<Company> companies = companyRepository.findAllById(ids);
        return companies.stream()
                .map(companyMapper::toShortDto)
                .toList();
    }
}
