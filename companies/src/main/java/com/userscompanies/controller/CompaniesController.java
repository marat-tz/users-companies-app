package com.userscompanies.controller;

import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;
import com.userscompanies.service.CompanyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/companies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompaniesController {

    final CompanyService companyService;

    @GetMapping("/{companyId}")
    public CompanyDtoFullResponse findCompanyById(@PathVariable Long companyId) {
        return companyService.findCompanyById(companyId);
    }

    @GetMapping
    public List<CompanyDtoFullResponse> findCompanies() {
        return companyService.findCompanies();
    }

    @GetMapping("/ids")
    public List<CompanyDtoShortResponse> findCompaniesByIds(@RequestParam(required = false) List<Long> ids) {
        return companyService.findCompaniesByIds(ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDtoShortResponse createCompany(@Valid @RequestBody CompanyDtoRequest dto) {
        return companyService.createCompany(dto);
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
    }

}
