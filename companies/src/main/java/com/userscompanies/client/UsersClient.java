package com.userscompanies.client;

import com.userscompanies.dto.UserDtoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "users", url = "${users.url}")
public interface UsersClient {
    @GetMapping("/users")
    List<UserDtoResponse> findUsersByCompanyIds(@RequestParam(required = false) List<Long> companyId);
}
