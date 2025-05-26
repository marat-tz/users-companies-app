package com.userscompanies.client;

import com.userscompanies.model.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "companies", url = "${companies.url}")
public interface CompaniesClient {
    @GetMapping("/companies/{companyId}")
    Optional<Company> findCompany(@PathVariable Long companyId);

    @GetMapping("/companies/ids")
    List<Company> findCompaniesByIds(@RequestParam(required = false) List<Long> ids);
}
