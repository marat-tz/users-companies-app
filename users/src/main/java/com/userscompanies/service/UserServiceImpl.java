package com.userscompanies.service;

import com.userscompanies.dto.UserDto;
import com.userscompanies.exception.ConflictException;
import com.userscompanies.exception.NotFoundException;
import com.userscompanies.mapper.UserMapper;
import com.userscompanies.model.User;
import com.userscompanies.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto dto) {
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new ConflictException("Пользователь с указанной почтой уже существует");
        }

        User user = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> findUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto findUserById(Long userId) {
        return userMapper.toDto(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь " + userId + " не существует")));
    }
}
