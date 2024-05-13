package org.walefy.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFixtures {
  public static Map<String, String> validUserCreate = Map.of(
      "name", "test",
      "email", "test@test.com",
      "password", "password",
      "image", ""
  );

  public static Map<String, String> userWithInvalidEmail = Map.of(
      "name", "test",
      "email", "t",
      "password", "password",
      "image", ""
  );

  public static Map<String, String> userWithoutEmail = Map.of(
      "name", "test",
      "password", "password",
      "image", ""
  );

  public static Map<String, String> userWithoutName = Map.of(
      "email", "test@test.com",
      "password", "password",
      "image", ""
  );

  public static Map<String, String> userWithInvalidName = Map.of(
      "name", "t",
      "email", "test@test.com",
      "password", "password",
      "image", ""
  );

  public static Map<String, String> userWithInvalidPassword = Map.of(
      "name", "test",
      "email", "test@test.com",
      "password", "a",
      "image", ""
  );

  public static Map<String, String> userWithoutPassword = Map.of(
      "name", "test",
      "email", "test@test.com",
      "image", ""
  );

  public static List<Map<String, String>> generateAListOfValidUsers(int quantity) {
    List<Map<String, String>> users = new ArrayList<>();

    for (int index = 0; index <= quantity; index++) {
      Map<String, String> user = new HashMap<>(Map.of(
          "name", "test",
          "password", "password",
          "image", ""
      ));

      user.put("email", String.format("test%d@test.com", index));
      users.add(user);
    }

    System.out.println(users);
    return users;
  }
}
