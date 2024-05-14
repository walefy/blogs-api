package org.walefy.exception;

public class PostNotFoundException extends NotFoundException {
  public PostNotFoundException() {
    super("Post not found!");
  }
}
