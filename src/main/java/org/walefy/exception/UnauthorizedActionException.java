package org.walefy.exception;

public class UnauthorizedActionException extends Exception {
  public UnauthorizedActionException(String action) {
    super("You are not allowed to: " + action);
  }
}
