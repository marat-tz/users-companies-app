package com.userscompanies.service;

import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {
    CompanyDtoShortResponse createCompany(CompanyDtoRequest dto);

    void deleteCompany(Long companyId);

    Page<CompanyDtoFullResponse> findCompanies(Integer from, Integer size);

    CompanyDtoShortResponse findCompanyByIdShort(Long companyId);

    CompanyDtoFullResponse findCompanyById(Long userId);

    Page<CompanyDtoShortResponse> findCompaniesByIds(List<Long> ids, Integer from, Integer size);

    CompanyDtoShortResponse updateCompanyById(CompanyDtoRequest dto, Long companyId);
}
