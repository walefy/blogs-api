package org.walefy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.walefy.mock.UserFixtures;
import org.walefy.mock.GenericJson;
import org.walefy.util.TestHelpers;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("<User> Integration test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @BeforeEach
  public void setup(WebApplicationContext wac) {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(wac)
        .apply(springSecurity())
        .build();
  }

  private GenericJson createUser(Map<String, Object> data, int statusCode) throws Exception {
    String responseJson = mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestHelpers.objectToJson(data))
    )
    .andExpect(status().is(statusCode))
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andReturn().getResponse().getContentAsString();

    return jsonMapper.readValue(responseJson, GenericJson.class);
  }

  @Test
  @DisplayName("must create a user")
  void testCreateUserSuccess() throws Exception {
    GenericJson expectedResponse = new GenericJson(Map.of(
        "id", 1,
        "name", "test",
        "email", "test@test.com",
        "image", ""
    ));

    GenericJson response = this.createUser(UserFixtures.validUserCreate, 201);

    assertEquals(expectedResponse, response);
  }

  @Test
  @DisplayName("shouldn't create two users with the same email")
  void testCreateTwoUserConflict() throws Exception {
    GenericJson expectedResponse = new GenericJson(Map.of("message", "User already registred!"));

    this.createUser(UserFixtures.validUserCreate, 201);
    GenericJson response = this.createUser(UserFixtures.validUserCreate, 409);

    assertEquals(expectedResponse, response);
  }

  @Test
  @DisplayName("shouldn't create user without email or with invalid email")
  void testCreateUserWithInvalidEmail() throws Exception {
    GenericJson expectedResponseInvalid = new GenericJson(Map.of(
        "message", "some invalid fields",
        "stack", List.of("email attribute must be a valid email")
    ));

    GenericJson expectedResponseWithout = new GenericJson(Map.of(
        "message", "some invalid fields",
        "stack", List.of("email attribute must no be blank")
    ));

    GenericJson responseWithoutEmail = this.createUser(UserFixtures.userWithoutEmail, 400);
    GenericJson responseWithInvalidEmail = this.createUser(UserFixtures.userWithInvalidEmail, 400);

    assertEquals(expectedResponseWithout, responseWithoutEmail);
    assertEquals(expectedResponseInvalid, responseWithInvalidEmail);
  }

  @Test
  @DisplayName("shouldn't create user without name or with invalid name")
  void testCreateUserWithInvalidName() throws Exception {
    GenericJson expectedResponseInvalid = new GenericJson(Map.of(
        "message", "some invalid fields",
        "stack", List.of("name must have more than 3 characters")
    ));

    GenericJson expectedResponseWithout = new GenericJson(Map.of(
        "message", "some invalid fields",
        "stack", List.of("name attribute must no be blank")
    ));

    GenericJson responseWithoutName = this.createUser(UserFixtures.userWithoutName, 400);
    GenericJson responseWithInvalidName = this.createUser(UserFixtures.userWithInvalidName, 400);

    assertEquals(expectedResponseWithout, responseWithoutName);
    assertEquals(expectedResponseInvalid, responseWithInvalidName);
  }

  @Test
  @DisplayName("shouldn't create user without password or with invalid password")
  void testCreateUserWithInvalidPassword() throws Exception {
    GenericJson expectedResponseInvalid = new GenericJson(Map.of(
        "message", "some invalid fields",
        "stack", List.of("password must have more than 6 characters")
    ));

    GenericJson expectedResponseWithout = new GenericJson(Map.of(
        "message", "some invalid fields",
        "stack", List.of("password attribute must no be blank")
    ));

    GenericJson responseWithoutPassword = this.createUser(UserFixtures.userWithoutPassword, 400);
    GenericJson responseWithInvalidPassword = this.createUser(UserFixtures.userWithInvalidPassword, 400);

    assertEquals(expectedResponseWithout, responseWithoutPassword);
    assertEquals(expectedResponseInvalid, responseWithInvalidPassword);
  }

  @Test
  @DisplayName("must list all users")
  void testListAllUserSuccess() throws Exception {
    List<GenericJson> users = UserFixtures.generateAListOfValidUsers(5);
    List<GenericJson> expectedUsers = new ArrayList<>();

    for (GenericJson user : users) {
      GenericJson userRes = this.createUser(user, 201);
      expectedUsers.add(userRes);
    }

    String responseJson = mockMvc.perform(get("/user")
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn().getResponse().getContentAsString();

    List<GenericJson> usersResponse = jsonMapper.readValue(
        responseJson,
        new TypeReference<>() {}
    );

    assertEquals(expectedUsers, usersResponse);
  }
}
