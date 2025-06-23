package com.userscompanies.controller;

import com.userscompanies.dto.UserDtoRequest;
import com.userscompanies.dto.UserDtoResponse;
import com.userscompanies.service.UserService;
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
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsersController {

    final UserService userService;

    @GetMapping("/{userId}")
    public UserDtoResponse findUserById(@PathVariable Long userId) {
        log.info("Получение пользователя с id = {}", userId);
        return userService.findUserById(userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse updateUserById(@RequestBody UserDtoRequest dto, @PathVariable Long userId) {
        log.info("Обновление пользователя с id = {}, содержимое входящего DTO = {}", userId, dto);
        return userService.updateUserById(dto, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoResponse createUser(@Valid @RequestBody UserDtoRequest dto) {
        log.info("Создание нового пользователя, содержимое входящего DTO = {}", dto);
        return userService.createUser(dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя с id = {}", userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public Page<UserDtoResponse> findUsers(@RequestParam(required = false) List<Long> companyId,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение списка всех пользователей с companyId = {}" +
                " (при отсутствии companyId - получение всех пользователей", companyId);
        return userService.findUsers(companyId, from, size);
    }
}
