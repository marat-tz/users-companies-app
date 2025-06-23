package com.userscompanies.controller;

import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;
import com.userscompanies.service.CompanyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/companies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompaniesController {

    final CompanyService companyService;

    @GetMapping("/{companyId}")
    public CompanyDtoFullResponse findCompanyById(@PathVariable Long companyId) {
        log.info("Получение компании по id = {}", companyId);
        return companyService.findCompanyById(companyId);
    }

    @GetMapping
    public Page<CompanyDtoFullResponse> findCompanies(@RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение списка всех компаний");
        return companyService.findCompanies(from, size);
    }

    @GetMapping("/ids")
    public List<CompanyDtoShortResponse> findCompaniesByIds(@RequestParam(required = false) List<Long> ids) {
        log.info("Получение компаний по списку id = {}", ids);
        return companyService.findCompaniesByIds(ids);
    }

    @PatchMapping("/{companyId}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyDtoShortResponse updateCompanyById(@RequestBody CompanyDtoRequest dto, @PathVariable Long companyId) {
        log.info("Обновление компании с id = {}, содержимое входящего DTO = {}", companyId, dto);
        return companyService.updateCompanyById(dto, companyId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDtoShortResponse createCompany(@Valid @RequestBody CompanyDtoRequest dto) {
        log.info("Создание новой компании, содержимое входящего DTO = {}", dto);
        return companyService.createCompany(dto);
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long companyId) {
        log.info("Удаление компании с id = {}", companyId);
        companyService.deleteCompany(companyId);
    }
}
