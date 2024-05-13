package org.walefy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.walefy.dto.CategoryCreationDto;
import org.walefy.entity.Category;
import org.walefy.repository.CategoryRepository;

@Service
public class CategoryService {
  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public Category create(CategoryCreationDto categoryCreation) {
    return this.categoryRepository.save(categoryCreation.toCategory());
  }
}
