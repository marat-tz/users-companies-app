package com.usercompanies.controller;

import com.usercompanies.dto.CompanyDto;
import com.usercompanies.service.CompanyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompaniesController {

    final CompanyService companyService;

    @GetMapping("/{userId}")
    public CompanyDto findCompanyById(@PathVariable Long userId) {
        return companyService.findCompanyById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDto createCompany(@Valid @RequestBody CompanyDto dto) {
        return companyService.createCompany(dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long userId) {
        companyService.deleteCompany(userId);
    }

    @GetMapping
    public List<CompanyDto> findCompanies() {
        return companyService.findCompanies();
    }

}
