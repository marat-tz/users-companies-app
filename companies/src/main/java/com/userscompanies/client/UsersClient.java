package com.userscompanies.client;

import com.userscompanies.dto.UserDtoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "users", url = "http://localhost:9090")
public interface UsersClient {
    @GetMapping("/users")
    Page<UserDtoResponse> findUsersByCompanyIds(@RequestParam(required = false) List<Long> companyId);
}
