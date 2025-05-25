package com.userscompanies.service;

import com.userscompanies.dto.CompanyDto;
import com.userscompanies.exception.ConflictException;
import com.userscompanies.exception.NotFoundException;
import com.userscompanies.mapper.CompanyMapper;
import com.userscompanies.model.Company;
import com.userscompanies.model.User;
import com.userscompanies.repository.CompanyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    public CompanyDto createCompany(CompanyDto dto) {
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
    public List<CompanyDto> findCompanies() {
        log.info("Получение всех компаний");
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(companyMapper::toDto)
                .toList();
    }

    @Override
    public CompanyDto findCompanyById(Long companyId) {
        log.info("Поиск компании по id");
        return companyMapper.toDto(companyRepository.findById(companyId).orElseThrow(() ->
                new NotFoundException("Компания " + companyId + " не существует")));
    }

}
