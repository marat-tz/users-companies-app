package com.userscompanies.service;

import com.userscompanies.client.CompaniesClient;
import com.userscompanies.dto.UserDto;
import com.userscompanies.exception.ConflictException;
import com.userscompanies.exception.NotFoundException;
import com.userscompanies.mapper.UserMapper;
import com.userscompanies.model.User;
import com.userscompanies.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    final CompaniesClient companiesClient;
    final UserRepository userRepository;
    final UserMapper userMapper;

    // TODO: добавить проверку на существование компании
    @Override
    public UserDto createUser(UserDto dto) {
        log.info("Создание пользователя");
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new ConflictException("Пользователь с указанной почтой уже существует");
        }

        User user = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление пользователя");
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> findUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto findUserById(Long userId) {
        log.info("Получение пользователя по id");
        return userMapper.toDto(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь " + userId + " не существует")));
    }
}
