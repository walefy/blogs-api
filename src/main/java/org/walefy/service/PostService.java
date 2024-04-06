package org.walefy.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.walefy.entity.Post;
import org.walefy.exception.PostNotFoundException;
import org.walefy.exception.UnauthorizedActionException;
import org.walefy.repository.PostRepository;

@Service
public class PostService {
  private final PostRepository postRepository;

  @Autowired
  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Transactional
  public void delete(long id, String email)
      throws PostNotFoundException, UnauthorizedActionException {
    Post post = this.postRepository
        .findById(id)
        .orElseThrow(PostNotFoundException::new);

    String ownerOfPostEmail = post.getUser().getEmail();

    if (!ownerOfPostEmail.equalsIgnoreCase(email)) {
      throw new UnauthorizedActionException("delete a post that is not yours");
    }

    this.postRepository.delete(post);
  }
}
