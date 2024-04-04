package org.walefy.exception;

public class UserAlreadyRegistred extends Exception {
  public UserAlreadyRegistred() {
    super("User already registred!");
  }
}
