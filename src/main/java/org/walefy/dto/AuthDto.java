package org.walefy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthDto(
    @NotBlank(message = "email attribute must not be blank")
    @Email(message = "email attribute must be a valid email")
    String email,
    @NotBlank(message = "password attribute must not be blank")
    String password
) {}
