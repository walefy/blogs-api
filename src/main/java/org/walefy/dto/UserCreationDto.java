package org.walefy.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.walefy.entity.User;

public record UserCreationDto(
  @NotBlank(message = "name attribute must not be blank")
  @Size(min = 3, message = "name must have more than 3 characters")
  String name,
  @NotBlank(message = "password attribute must not be blank")
  @Size(min = 6, message = "password must have more than 6 characters")
  String password,
  @NotBlank(message = "email attribute must not be blank")
  @Email(message = "email attribute must be a valid email")
  String email,
  @Nullable
  String image
) {
  public User toUser() {
    return new User(
      this.name,
      this.email,
      this.password,
      this.image
    );
  }
}
