package org.walefy.exception;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException() {
    super("User not found");
  }
}
