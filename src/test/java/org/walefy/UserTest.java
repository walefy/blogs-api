package org.walefy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.walefy.mock.UserFixtures;
import org.walefy.mock.UserMock;
import org.walefy.util.TestHelpers;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("<User> Integration test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserTest {
  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper jsonMapper = new ObjectMapper();

  private UserMock createUser(Map<String, Object> data, int statusCode) throws Exception {
    String responseJson = mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestHelpers.objectToJson(data))
    )
    .andExpect(status().is(statusCode))
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andReturn().getResponse().getContentAsString();

    return jsonMapper.readValue(responseJson, UserMock.class);
  }

  @Test
  @DisplayName("must create a user")
  void testCreateUserSuccess() throws Exception {
    UserMock expectedResponse = new UserMock(Map.of(
        "id", 1,
        "name", "test",
        "email", "test@test.com",
        "image", ""
    ));

    UserMock response = this.createUser(UserFixtures.validUserCreate, 201);

    assertEquals(response, expectedResponse);
  }

  @Test
  @DisplayName("shouldn't create two users with the same email")
  void testCreateTwoUserConflict() throws Exception {
    UserMock expectedResponse = new UserMock(Map.of("message", "User already registred!"));

    this.createUser(UserFixtures.validUserCreate, 201);
    UserMock response = this.createUser(UserFixtures.validUserCreate, 409);

    assertEquals(response, expectedResponse);
  }

  @Test
  @DisplayName("shouldn't create user without email or with invalid email")
  void testCreateUserWithInvalidEmail() throws Exception {
    UserMock expectedResponseInvalid = new UserMock(Map.of(
        "message", "email attribute must be a valid email"
    ));

    UserMock expectedResponseWithout = new UserMock(Map.of(
        "message", "email attribute must no be blank"
    ));

    UserMock responseWithoutEmail = this.createUser(UserFixtures.userWithoutEmail, 400);
    UserMock responseWithInvalidEmail = this.createUser(UserFixtures.userWithInvalidEmail, 400);

    assertEquals(expectedResponseWithout, responseWithoutEmail);
    assertEquals(expectedResponseInvalid, responseWithInvalidEmail);
  }

  @Test
  @DisplayName("shouldn't create user without name or with invalid name")
  void testCreateUserWithInvalidName() throws Exception {
    UserMock expectedResponseInvalid = new UserMock(Map.of(
        "message", "name must have more than 3 characters"
    ));

    UserMock expectedResponseWithout = new UserMock(Map.of(
        "message", "name attribute must no be blank"
    ));

    UserMock responseWithoutName = this.createUser(UserFixtures.userWithoutName, 400);
    UserMock responseWithInvalidName = this.createUser(UserFixtures.userWithInvalidName, 400);

    assertEquals(expectedResponseWithout, responseWithoutName);
    assertEquals(expectedResponseInvalid, responseWithInvalidName);
  }

  @Test
  @DisplayName("shouldn't create user without password or with invalid password")
  void testCreateUserWithInvalidPassword() throws Exception {
    UserMock expectedResponseInvalid = new UserMock(Map.of(
        "message", "password must have more than 6 characters"
    ));

    UserMock expectedResponseWithout = new UserMock(Map.of(
        "message", "password attribute must no be blank"
    ));

    UserMock responseWithoutPassword = this.createUser(UserFixtures.userWithoutPassword, 400);
    UserMock responseWithInvalidPassword = this.createUser(UserFixtures.userWithInvalidPassword, 400);

    assertEquals(expectedResponseWithout, responseWithoutPassword);
    assertEquals(expectedResponseInvalid, responseWithInvalidPassword);
  }
}
