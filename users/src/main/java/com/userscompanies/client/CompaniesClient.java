package com.userscompanies.client;

import com.userscompanies.model.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "companies")
public interface CompaniesClient {
    @GetMapping("/companies/{companyId}")
    ResponseEntity<Company> findCompany(@PathVariable Long companyId);

    @GetMapping("/companies/ids")
    List<Company> findCompaniesByIds(@RequestParam(required = false) List<Long> ids);
}
