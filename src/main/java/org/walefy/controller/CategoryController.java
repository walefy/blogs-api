package org.walefy.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.walefy.dto.CategoryCreationDto;
import org.walefy.dto.CategoryReturnDto;
import org.walefy.dto.PostReturnDto;
import org.walefy.entity.Category;
import org.walefy.exception.CategoryNotFound;
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
  public ResponseEntity<CategoryReturnDto> create(
    @RequestBody @Valid CategoryCreationDto categoryCreation) {
    Category category = this.categoryService.create(categoryCreation);

    return ResponseEntity.ok(CategoryReturnDto.fromEntity(category));
  }

  @GetMapping
  public ResponseEntity<List<CategoryReturnDto>> findAll() {
    List<CategoryReturnDto> categories = this.categoryService
      .findAll()
      .stream()
      .map(CategoryReturnDto::fromEntity)
      .toList();

    return ResponseEntity.ok(categories);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryReturnDto> findById(@PathVariable Long id) throws CategoryNotFound {
    return ResponseEntity.ok(CategoryReturnDto.fromEntity(this.categoryService.findById(id)));
  }

  @GetMapping("/{id}/posts")
  public ResponseEntity<List<PostReturnDto>> findPostsByCategory(
    @PathVariable Long id
  ) throws CategoryNotFound {
    List<PostReturnDto> posts = this.categoryService.findPostsByCategory(id)
      .stream()
      .map(PostReturnDto::fromEntity)
      .toList();

    return ResponseEntity.ok(posts);
  }
}
