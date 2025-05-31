package com.userscompanies.service;

import com.userscompanies.dto.UserDtoRequest;
import com.userscompanies.dto.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDtoResponse createUser(UserDtoRequest dto);

    void deleteUser(Long userId);

    List<UserDtoResponse> findUsers(List<Long> companyId);

    UserDtoResponse findUserById(Long userId);
}
