package org.walefy.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.walefy.dto.PostCreationDto;
import org.walefy.dto.UserCreationDto;
import org.walefy.entity.Post;
import org.walefy.entity.User;
import org.walefy.exception.UserAlreadyRegistred;
import org.walefy.exception.UserNotFoundException;
import org.walefy.repository.PostRepository;
import org.walefy.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
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

  @Transactional
  public void deleteByEmail(String email) {
    this.userRepository.deleteByEmail(email);
  }

  @Transactional
  public Post addPost(PostCreationDto postCreation, String email) throws UserNotFoundException {
    User user = this.userRepository
        .findByEmail(email)
        .orElseThrow(UserNotFoundException::new);

    Post post = postCreation.toPost();
    user.getPosts().add(post);

    this.userRepository.save(user);

    return post;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return this.userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));
  }
}
