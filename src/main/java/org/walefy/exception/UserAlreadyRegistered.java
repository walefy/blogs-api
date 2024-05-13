package org.walefy.exception;

public class UserAlreadyRegistered extends Exception {
  public UserAlreadyRegistered() {
    super("User already registered!");
  }
}
