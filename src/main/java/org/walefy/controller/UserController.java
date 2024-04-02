package org.walefy.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.walefy.dto.UserCreationDto;
import org.walefy.dto.UserReturnDto;
import org.walefy.entity.User;
import org.walefy.exception.UserAlreadyRegistred;
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
  ) throws UserAlreadyRegistred {
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
}
