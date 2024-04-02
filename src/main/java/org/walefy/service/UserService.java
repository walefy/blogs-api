package org.walefy.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.walefy.dto.UserCreationDto;
import org.walefy.entity.User;
import org.walefy.exception.UserAlreadyRegistred;
import org.walefy.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User create(UserCreationDto userCreation) throws UserAlreadyRegistred {
    Optional<User> userExists = this.userRepository.findByEmail(userCreation.email());

    if (userExists.isPresent()) {
      throw new UserAlreadyRegistred();
    }

    return this.userRepository.save(userCreation.toUser());
  }

  public List<User> findAll() {
    return this.userRepository.findAll();
  }
}
