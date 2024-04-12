package org.walefy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.walefy.dto.PostReturnDto;
import org.walefy.exception.PostNotFoundException;
import org.walefy.exception.UnauthorizedActionException;
import org.walefy.service.PostService;

@RestController
@RequestMapping("/post")
public class PostController {
  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable long id, Authentication authentication)
      throws PostNotFoundException, UnauthorizedActionException {
    this.postService.delete(id, authentication.getName());

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<List<PostReturnDto>> findAll() {
    List<PostReturnDto> posts = this.postService
      .findAll()
      .stream()
      .map(PostReturnDto::postToPostReturnDto)
      .toList();

    return ResponseEntity.ok(posts);
  }
}
