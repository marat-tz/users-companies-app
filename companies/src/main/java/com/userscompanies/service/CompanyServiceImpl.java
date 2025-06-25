package com.userscompanies.service;

import com.userscompanies.client.UsersClient;
import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;
import com.userscompanies.dto.UserDtoResponse;
import com.userscompanies.exception.ConflictException;
import com.userscompanies.exception.NotFoundException;
import com.userscompanies.mapper.CommonCompanyMapper;
import com.userscompanies.mapper.CompanyMapper;
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

    final CompanyRepository companyRepository;
    final CompanyMapper companyMapper;
    final UsersClient usersClient;

    @Override
    public CompanyDtoShortResponse createCompany(CompanyDtoRequest dto) {
        checkCompanyExistsByName(dto.getName());
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
        Pageable pageable = PageRequest.of(from, size);
        Page<Company> companies = companyRepository.findAll(pageable);

        List<Long> companiesIds = companies.getContent()
                .stream()
                .map(Company::getId)
                .toList();

        Page<UserDtoResponse> users = usersClient.findUsersByCompanyIds(companiesIds);
        Map<Long, List<UserDtoResponse>> userMap = users.getContent()
                .stream()
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
        Company company = getCompanyById(companyId);

        Page<UserDtoResponse> users = usersClient.findUsersByCompanyIds(List.of(companyId));
        CompanyDtoFullResponse result = companyMapper.toDto(company, users.getContent());

        log.info("Найдена компания с id = {}, DTO: {}", companyId, result);
        return result;
    }

    @Override
    public Page<CompanyDtoShortResponse> findCompaniesByIds(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Company> companies = companyRepository.findAllByIdIn(pageable, ids);

        List<CompanyDtoShortResponse> result = companies.getContent()
                .stream()
                .map(companyMapper::toShortDto)
                .toList();

        Page<CompanyDtoShortResponse> pageResult = new PageImpl<>(
                result,
                companies.getPageable(),
                companies.getTotalElements()
        );

        log.info("Найдены компании с id = {}, в количестве: {}", ids, result.size());
        return pageResult;
    }

    @Override
    public CompanyDtoShortResponse updateCompanyById(CompanyDtoRequest dto, Long companyId) {

        Company company = getCompanyById(companyId);

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

    private void checkCompanyExistsByName(String name) {
        if (companyRepository.existsByName(name)) {
            throw new ConflictException("Компания с указанным названием уже существует");
        }
    }

    private Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Компания " + id + " не существует"));
    }
}
