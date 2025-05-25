package com.userscompanies.client;

import com.userscompanies.model.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "companies", url = "${companies.url}")
public interface CompaniesClient {
    @GetMapping("/companies/{companyId}")
    Company findCompany(@PathVariable Long companyId);
}
