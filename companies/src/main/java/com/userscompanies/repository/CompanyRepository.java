package com.userscompanies.repository;

import com.userscompanies.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String name);
    Page<Company> findAllByIdIn(Pageable pageable, List<Long> companyIds);
}
