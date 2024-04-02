package org.walefy.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.walefy.dto.UserCreationDto;
import org.walefy.entity.User;
import org.walefy.exception.UserAlreadyRegistred;
import org.walefy.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
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

    User user = userCreation.toUser();
    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

    return this.userRepository.save(user);
  }

  public List<User> findAll() {
    return this.userRepository.findAll();
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return this.userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));
  }
}
