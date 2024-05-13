package org.walefy.dto;

import org.walefy.entity.Category;

public record CategoryReturnDto(
  Long id,
  String name
) {
  public CategoryReturnDto fromEntity(Category category) {
    return new CategoryReturnDto(category.getId(), category.getName());
  }
}
