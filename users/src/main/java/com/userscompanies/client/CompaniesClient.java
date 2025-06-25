package com.userscompanies.client;

import com.userscompanies.dto.CompanyDtoShortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "companies", url = "http://localhost:9091")
public interface CompaniesClient {
    @GetMapping("/companies/{companyId}")
    ResponseEntity<CompanyDtoShortResponse> findCompany(@PathVariable Long companyId);

    @GetMapping("/companies/ids")
    Page<CompanyDtoShortResponse> findCompaniesByIds(@RequestParam(required = false) List<Long> ids);
}
