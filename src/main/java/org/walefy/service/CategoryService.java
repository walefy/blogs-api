package org.walefy.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.walefy.dto.CategoryCreationDto;
import org.walefy.entity.Category;
import org.walefy.entity.Post;
import org.walefy.exception.CategoryNotFound;
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

  public List<Category> findAll() {
    return this.categoryRepository.findAll();
  }

  public Category findById(Long id) throws CategoryNotFound {
    return this.categoryRepository.findById(id).orElseThrow(CategoryNotFound::new);
  }

  public List<Post> findPostsByCategory(Long id) throws CategoryNotFound {
    Category category = this.findById(id);

    return category.getPosts();
  }
}
