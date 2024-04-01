package org.walefy.dto;

import org.walefy.entity.User;

public record UserReturnDto(
    Long id,
    String name,
    String email,
    String image
) {
  public static UserReturnDto UserToUserReturnDto(User user) {
    return new UserReturnDto(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getImage()
    );
  }
}
