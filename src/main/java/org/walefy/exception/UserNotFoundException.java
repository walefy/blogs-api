package org.walefy.exception;

public class UserNotFoundException extends Exception {
  public UserNotFoundException() {
    super("User not found");
  }
}
