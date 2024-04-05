package org.walefy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email", unique = true)
})
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "user")
  private List<Post> posts = new ArrayList<>();

  private String image;

  public User(Long id, String name, String email, String password, String image) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.image = image;
  }

  public User(String name, String email, String password, String image) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.image = image;
  }

  public User() {}

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return this.email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Post> getPosts() {
    return this.posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }
}
