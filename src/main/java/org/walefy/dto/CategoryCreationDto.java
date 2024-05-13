package org.walefy.dto;

import org.walefy.entity.Category;

public record CategoryCreationDto(String name) {
  public Category toCategory() {
    return new Category(
      name
    );
  }
}
