package com.usercompanies.service;

import com.usercompanies.dto.CompanyDto;
import com.usercompanies.exception.ConflictException;
import com.usercompanies.mapper.CompanyMapper;
import com.usercompanies.model.Company;
import com.usercompanies.repository.CompanyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyServiceImpl implements CompanyService {

    final CompanyRepository companyRepository;
    final CompanyMapper companyMapper;

    @Override
    public CompanyDto createCompany(CompanyDto dto) {
        if (companyRepository.existsByName(dto.getName())) {
            throw new ConflictException("Компания с указанным названием уже существует");
        }

        Company company = companyRepository.save(companyMapper.toEntity(dto));
        return companyMapper.toDto(company);
    }

    @Override
    public void deleteCompany(Long companyId) {

    }

    @Override
    public List<CompanyDto> findCompanies() {
        return List.of();
    }

    @Override
    public CompanyDto findCompanyById(Long userId) {
        return null;
    }

}
