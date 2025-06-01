package com.userscompanies.service;

import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CompanyService {
    CompanyDtoShortResponse createCompany(CompanyDtoRequest dto);

    void deleteCompany(Long companyId);

    List<CompanyDtoFullResponse> findCompanies();

    CompanyDtoFullResponse findCompanyById(Long userId);

    List<CompanyDtoShortResponse> findCompaniesByIds(List<Long> ids);

    CompanyDtoShortResponse updateCompanyById(CompanyDtoRequest dto, Long companyId);
}
