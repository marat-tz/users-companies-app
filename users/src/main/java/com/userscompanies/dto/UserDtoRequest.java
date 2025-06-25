package com.userscompanies.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDtoRequest {
    Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    String firstName;

    @NotBlank
    @Size(min = 1, max = 150)
    String lastName;

    @NotBlank
    @Size(min = 1, max = 50)
    String phone;

    @NotNull
    Long companyId;
}
