package org.walefy.exception;

public class CategoryNotFound extends NotFoundException {
  public CategoryNotFound() {
    super("Category not found!");
  }
}
