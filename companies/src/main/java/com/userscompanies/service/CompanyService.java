package com.userscompanies.service;

import com.userscompanies.dto.CompanyDto;

import java.util.List;

public interface CompanyService {
    CompanyDto createCompany(CompanyDto dto);

    void deleteCompany(Long companyId);

    List<CompanyDto> findCompanies();

    CompanyDto findCompanyById(Long userId);
}
