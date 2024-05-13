package org.walefy.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.walefy.dto.PostCreationDto;
import org.walefy.dto.PostReturnDto;
import org.walefy.dto.UserCreationDto;
import org.walefy.dto.UserReturnDto;
import org.walefy.entity.Post;
import org.walefy.entity.User;
import org.walefy.exception.CategoryNotFound;
import org.walefy.exception.UserAlreadyRegistered;
import org.walefy.exception.UserNotFoundException;
import org.walefy.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<UserReturnDto> create(
      @RequestBody @Valid UserCreationDto userCreation
  ) throws UserAlreadyRegistered {
    User user = this.userService.create(userCreation);

    return ResponseEntity.status(HttpStatus.CREATED).body(UserReturnDto.UserToUserReturnDto(user));
  }

  @GetMapping
  public ResponseEntity<List<UserReturnDto>> findAll() {
    List<UserReturnDto> users = this.userService
        .findAll()
        .stream()
        .map(UserReturnDto::UserToUserReturnDto)
        .toList();

    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @DeleteMapping
  public ResponseEntity<Map<String, String>> delete(Authentication authentication) {
    String email = authentication.getName();

    this.userService.deleteByEmail(email);

    return ResponseEntity.ok(Map.of("message", "User deleted!"));
  }

  @PostMapping("/post")
  public ResponseEntity<PostReturnDto> addPost(
      @RequestBody @Valid PostCreationDto postCreation,
      Authentication authentication
  )
      throws UserNotFoundException, CategoryNotFound {
    Post post = this.userService.addPost(postCreation, authentication.getName());

    return ResponseEntity.status(HttpStatus.CREATED).body(PostReturnDto.fromEntity(post));
  }

  @GetMapping("/post")
  public ResponseEntity<List<PostReturnDto>> findAllPosts(Authentication authentication)
    throws UserNotFoundException {
    List<PostReturnDto> posts = this.userService
      .findAllPosts(authentication.getName())
      .stream()
      .map(PostReturnDto::fromEntity)
      .toList();

    return ResponseEntity.ok(posts);
  }

  @GetMapping("/{id}/post")
  public ResponseEntity<List<PostReturnDto>> findAllPosts(@PathVariable long id)
    throws UserNotFoundException {
    List<PostReturnDto> posts = this.userService
      .findAllPosts(id)
      .stream()
      .map(PostReturnDto::fromEntity)
      .toList();

    return ResponseEntity.ok(posts);
  }
}
