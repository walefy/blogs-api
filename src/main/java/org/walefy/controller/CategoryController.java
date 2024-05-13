package org.walefy.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.walefy.dto.CategoryCreationDto;
import org.walefy.entity.Category;
import org.walefy.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  public ResponseEntity<Category> create(@RequestBody @Valid CategoryCreationDto categoryCreation) {
    Category category = this.categoryService.create(categoryCreation);

    return ResponseEntity.ok(category);
  }
}
