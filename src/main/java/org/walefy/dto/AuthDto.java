package org.walefy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthDto(
    @NotNull
    @Email
    String email,
    @NotBlank
    String password
) {}
