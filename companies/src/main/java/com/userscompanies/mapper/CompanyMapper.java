package com.userscompanies.mapper;

import com.userscompanies.dto.CompanyDto;
import com.userscompanies.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "budget", source = "budget")
    CompanyDto toDto(Company company);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "budget", source = "budget")
    Company toEntity(CompanyDto dto);

}
