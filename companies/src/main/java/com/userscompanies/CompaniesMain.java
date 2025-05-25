package com.userscompanies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CompaniesMain {
    public static void main(String[] args) {
        SpringApplication.run(CompaniesMain.class, args);
    }
}
