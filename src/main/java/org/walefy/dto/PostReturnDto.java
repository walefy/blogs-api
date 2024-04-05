package org.walefy.dto;

import java.time.LocalDateTime;
import org.walefy.entity.Post;

public record PostReturnDto(
    Long id,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
  public static PostReturnDto postToPostReturnDto(Post post) {
    return new PostReturnDto(
        post.getId(),
        post.getTitle(),
        post.getContent(),
        post.getCreatedAt(),
        post.getUpdatedAt()
    );
  }
}
