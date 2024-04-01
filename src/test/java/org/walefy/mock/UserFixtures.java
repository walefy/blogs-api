package org.walefy.mock;

import java.util.Map;

public class UserFixtures {
  public static Map<String, Object> validUserCreate = Map.of(
      "name", "test",
      "email", "test@test.com",
      "password", "password",
      "image", ""
  );

  public static Map<String, Object> userWithInvalidEmail = Map.of(
      "name", "test",
      "email", "t",
      "password", "password",
      "image", ""
  );

  public static Map<String, Object> userWithoutEmail = Map.of(
      "name", "test",
      "password", "password",
      "image", ""
  );

  public static Map<String, Object> userWithoutName = Map.of(
      "email", "test@test.com",
      "password", "password",
      "image", ""
  );

  public static Map<String, Object> userWithInvalidName = Map.of(
      "name", "t",
      "email", "test@test.com",
      "password", "password",
      "image", ""
  );

  public static Map<String, Object> userWithInvalidPassword = Map.of(
      "name", "test",
      "email", "test@test.com",
      "password", "a",
      "image", ""
  );

  public static Map<String, Object> userWithoutPassword = Map.of(
      "name", "test",
      "email", "test@test.com",
      "image", ""
  );

}
