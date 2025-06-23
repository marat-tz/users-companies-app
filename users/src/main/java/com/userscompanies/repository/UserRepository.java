package com.userscompanies.repository;

import com.userscompanies.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhone(String phone);
    Page<User> findAllByCompanyIdIn(Pageable pageable, List<Long> companyId);
}
