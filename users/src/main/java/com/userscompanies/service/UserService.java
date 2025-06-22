package com.userscompanies.service;

import com.userscompanies.dto.UserDtoRequest;
import com.userscompanies.dto.UserDtoResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {
    UserDtoResponse createUser(UserDtoRequest dto);

    void deleteUser(Long userId);

    List<UserDtoResponse> findUsers(List<Long> companyId);

    UserDtoResponse findUserById(Long userId);

    UserDtoResponse updateUserById(UserDtoRequest dto, Long userId);
}
