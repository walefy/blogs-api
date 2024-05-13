package org.walefy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.walefy.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
