package com.userscompanies.service;

import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;
import com.userscompanies.exception.ConflictException;
import com.userscompanies.exception.NotFoundException;
import com.userscompanies.mapper.CompanyMapper;
import com.userscompanies.model.Company;
import com.userscompanies.repository.CompanyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyServiceImpl implements CompanyService {

    final CompanyRepository companyRepository;
    final CompanyMapper companyMapper;

    @Override
    public CompanyDtoFullResponse createCompany(CompanyDtoRequest dto) {
        log.info("Создание компании");
        if (companyRepository.existsByName(dto.getName())) {
            throw new ConflictException("Компания с указанным названием уже существует");
        }

        Company company = companyRepository.save(companyMapper.toEntity(dto));
        return companyMapper.toDto(company);
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
        return companies.stream()
                .map(companyMapper::toDto)
                .toList();
    }

    @Override
    public CompanyDtoFullResponse findCompanyById(Long companyId) {
        log.info("Поиск компании по id");
        return companyMapper.toDto(companyRepository.findById(companyId).orElseThrow(() ->
                new NotFoundException("Компания " + companyId + " не существует")));
    }

    @Override
    public List<CompanyDtoShortResponse> findCompaniesByIds(List<Long> ids) {
        List<Company> companies = companyRepository.findAllById(ids);
        return companies.stream()
                .map(companyMapper::toShortDto)
                .toList();
    }

}
