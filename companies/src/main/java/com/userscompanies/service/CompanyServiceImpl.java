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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if (companyRepository.existsByName(dto.getName())) {
            throw new ConflictException("Компания с указанным названием уже существует");
        }

        Company company = companyRepository.save(companyMapper.toEntity(dto));
        CompanyDtoShortResponse result = companyMapper.toShortDto(company);
        log.info("Создана компания {}", result);
        return result;
    }

    @Override
    public void deleteCompany(Long companyId) {
        companyRepository.deleteById(companyId);
        log.info("Удалена компания с id = {}", companyId);
    }

    @Override
    public Page<CompanyDtoFullResponse> findCompanies(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Company> companies = companyRepository.findAll(pageable);

        List<Long> companiesIds = companies.getContent()
                .stream()
                .map(Company::getId)
                .toList();

        Page<UserDtoResponse> users = usersClient.findUsersByCompanyIds(companiesIds);
        Map<Long, List<UserDtoResponse>> userMap = users.getContent().stream()
                .collect(Collectors.groupingBy(user -> user.getCompany().getId()));

        List<CompanyDtoFullResponse> result = companies.getContent()
                .stream()
                .map(company -> companyMapper.toDto(company, userMap.get(company.getId())))
                .toList();

        Page<CompanyDtoFullResponse> pageResult = new PageImpl<>(
                result,
                companies.getPageable(),
                companies.getTotalElements()
        );

        log.info("Получен список всех компаний в количестве: {}", result.size());
        return pageResult;
    }

    @Override
    public CompanyDtoFullResponse findCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new NotFoundException("Компания " + companyId + " не существует"));

        Page<UserDtoResponse> users = usersClient.findUsersByCompanyIds(List.of(companyId));
        CompanyDtoFullResponse result = companyMapper.toDto(company, users.getContent());

        log.info("Найдена компания с id = {}, DTO: {}", companyId, result);
        return result;
    }

    @Override
    public List<CompanyDtoShortResponse> findCompaniesByIds(List<Long> ids) {
        List<Company> companies = companyRepository.findAllById(ids);
        List<CompanyDtoShortResponse> result = companies.stream()
                .map(companyMapper::toShortDto)
                .toList();

        log.info("Найдены компании с id = {}, в количестве: {}", ids, result.size());
        return result;
    }

    @Override
    public CompanyDtoShortResponse updateCompanyById(CompanyDtoRequest dto, Long companyId) {

        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new NotFoundException("Компания " + companyId + " не существует"));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            company.setName(dto.getName());
        }

        if (dto.getBudget() != null && dto.getBudget() >= 0) {
            company.setBudget(dto.getBudget());
        }

        Company savedCompany = companyRepository.save(company);
        CompanyDtoShortResponse result = companyMapper.toShortDto(savedCompany);
        log.info("Обновлена компания, возвращаемый DTO: {}", result);
        return result;
    }
}
