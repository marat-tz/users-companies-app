package com.userscompanies.service;

import com.userscompanies.dto.UserDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto dto);

    void deleteUser(Long userId);

    List<UserDto> findUsers();

    UserDto findUserById(Long userId);
}
