package org.walefy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.walefy.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
