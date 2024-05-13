package org.walefy.dto;

import java.time.LocalDateTime;
import java.util.List;
import org.walefy.entity.Post;

public record PostReturnDto(
  Long id,
  String title,
  String content,
  List<CategoryReturnDto> categories,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {
  public static PostReturnDto postToPostReturnDto(Post post) {
    List<CategoryReturnDto> categoryReturn = post
      .getCategories()
      .stream()
      .map((c) -> new CategoryReturnDto(c.getId(), c.getName()))
      .toList();

    return new PostReturnDto(
      post.getId(),
      post.getTitle(),
      post.getContent(),
      categoryReturn,
      post.getCreatedAt(),
      post.getUpdatedAt()
    );
  }
}
