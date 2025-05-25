package com.userscompanies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UsersService {
    public static void main(String[] args) {
        SpringApplication.run(UsersService.class, args);
    }
}
