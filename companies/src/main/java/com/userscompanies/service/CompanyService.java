package com.userscompanies.service;

import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;

import java.util.List;

public interface CompanyService {
    CompanyDtoShortResponse createCompany(CompanyDtoRequest dto);

    void deleteCompany(Long companyId);

    List<CompanyDtoFullResponse> findCompanies();

    CompanyDtoFullResponse findCompanyById(Long userId);

    List<CompanyDtoShortResponse> findCompaniesByIds(List<Long> ids);

    CompanyDtoShortResponse updateCompanyById(CompanyDtoRequest dto, Long companyId);
}
