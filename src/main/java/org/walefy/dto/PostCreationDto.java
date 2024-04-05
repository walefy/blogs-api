package org.walefy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.walefy.entity.Post;

public record PostCreationDto(
    @NotBlank(message = "title attribute must not be blank")
    @Size(min = 5, message =  "title must have more than 5 characters")
    String title,
    @NotBlank(message = "content attribute must not be blank")
    String content
) {
  public Post toPost() {
    return new Post(
        this.title(),
        this.content()
    );
  }
}
