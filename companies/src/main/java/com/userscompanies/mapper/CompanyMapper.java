package com.userscompanies.mapper;

import com.userscompanies.dto.CompanyDtoFullResponse;
import com.userscompanies.dto.CompanyDtoRequest;
import com.userscompanies.dto.CompanyDtoShortResponse;
import com.userscompanies.dto.UserDtoResponse;
import com.userscompanies.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "budget", source = "budget")
    CompanyDtoShortResponse toShortDto(Company company);

    @Mapping(target = "id", source = "company.id")
    @Mapping(target = "name", source = "company.name")
    @Mapping(target = "budget", source = "company.budget")
    @Mapping(target = "users", source = "users")
    CompanyDtoFullResponse toDto(Company company, List<UserDtoResponse> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "budget", source = "budget")
    Company toEntity(CompanyDtoRequest dto);

}
