package org.walefy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.walefy.mock.GenericJson;
import org.walefy.mock.UserFixtures;
import org.walefy.util.TestHelpers;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("<Post> Integration test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostTest {
  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper jsonMapper = new ObjectMapper();

  private String login(Map<String, String> payload) throws Exception {
    String loginResponse = this.mockMvc.perform(
        post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestHelpers.objectToJson(payload))
      )
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andReturn().getResponse().getContentAsString();

    GenericJson loginMap = jsonMapper.readValue(loginResponse, GenericJson.class);
    return (String) loginMap.get("token");
  }

  private GenericJson makeRequest(
    String endpoint,
    Map<String, String> data,
    String token,
    int status
  ) throws Exception {
    RequestBuilder postRequest;

    if (token != null) {
      postRequest = post(endpoint)
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestHelpers.objectToJson(data));
    } else {
      postRequest = post(endpoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestHelpers.objectToJson(data));
    }

    String responseJson = this.mockMvc.perform(postRequest)
      .andExpect(status().is(status))
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andReturn().getResponse().getContentAsString();

    return jsonMapper.readValue(responseJson, GenericJson.class);
  }

  @Test
  @DisplayName("should add a new post")
  public void addNewPostSuccess() throws Exception {
    this.makeRequest("/user", UserFixtures.validUserCreate, null, 201);
    Map<String, String> loginPayload = Map.of(
      "email", UserFixtures.validUserCreate.get("email"),
      "password", UserFixtures.validUserCreate.get("password")
    );
    String token = this.login(loginPayload);

    Map<String, String> postPayload = Map.of("title", "test title", "content", "test content");
    GenericJson post = this.makeRequest("/user/post", postPayload, token, 201);

    assertEquals(post.get("id"), 1);
    assertEquals(post.get("title"), "test title");
    assertEquals(post.get("content"), "test content");
    assertNotEquals(post.get("createdAt"), null);
    assertNotEquals(post.get("updatedAt"), null);
  }

  @Test
  @DisplayName("should delete a post")
  public void deletePostSuccess() throws Exception {
    this.makeRequest("/user", UserFixtures.validUserCreate, null, 201);
    Map<String, String> loginPayload = Map.of(
      "email", UserFixtures.validUserCreate.get("email"),
      "password", UserFixtures.validUserCreate.get("password")
    );
    String token = this.login(loginPayload);

    Map<String, String> postPayload = Map.of("title", "test title", "content", "test content");
    GenericJson post = this.makeRequest("/user/post", postPayload, token, 201);

    RequestBuilder deleteRequest = delete("/post/" + post.get("id"))
      .header("Authorization", "Bearer " + token);

    this.mockMvc.perform(deleteRequest)
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("shouldn't create post without title or without content")
  public void addNewPostWithoutTitleOrContent() throws Exception {
    this.makeRequest("/user", UserFixtures.validUserCreate, null, 201);
    Map<String, String> loginPayload = Map.of(
      "email", UserFixtures.validUserCreate.get("email"),
      "password", UserFixtures.validUserCreate.get("password")
    );
    String token = this.login(loginPayload);

    GenericJson expectedResponseWithoutTitle = new GenericJson(Map.of(
      "message", "some invalid fields",
      "stack", List.of(
        "title attribute must not be blank"
      )
    ));
    GenericJson expectedResponseWithoutContent = new GenericJson(Map.of(
      "message", "some invalid fields",
      "stack", List.of(
        "content attribute must not be blank"
      )
    ));

    Map<String, String> postPayloadWithoutTitle = Map.of(
      "content",
      "test content"
    );
    Map<String, String> postPayloadWithoutContent = Map.of(
      "title",
      "test title"
    );

    GenericJson postWithoutContent = this.makeRequest("/user/post", postPayloadWithoutContent, token, 400);
    GenericJson postWithoutTitle = this.makeRequest("/user/post", postPayloadWithoutTitle, token, 400);

    assertEquals(expectedResponseWithoutContent, postWithoutContent);
    assertEquals(expectedResponseWithoutTitle, postWithoutTitle);
  }

  @Test
  @DisplayName("shouldn't create post with another user token")
  public void deleteAnotherUsersPost() throws Exception {
    this.makeRequest("/user", UserFixtures.validUserCreate, null, 201);
    Map<String, String> loginPayloadOwner = Map.of(
      "email", UserFixtures.validUserCreate.get("email"),
      "password", UserFixtures.validUserCreate.get("password")
    );
    String tokenOwner = this.login(loginPayloadOwner);

    Map<String, String> postPayload = Map.of("title", "test title", "content", "test content");
    GenericJson post = this.makeRequest("/user/post", postPayload, tokenOwner, 201);

    Map<String, String> notOwnerUser = Map.of(
      "name", "testNotOwner",
      "email", "not@owner.com",
      "password", "password",
      "image", ""
    );

    this.makeRequest("/user", notOwnerUser, null, 201);
    Map<String, String> loginPayloadNotOwner = Map.of(
      "email", notOwnerUser.get("email"),
      "password", notOwnerUser.get("password")
    );
    String tokenNotOwner = this.login(loginPayloadNotOwner);

    RequestBuilder deleteRequest = delete("/post/" + post.get("id"))
      .header("Authorization", "Bearer " + tokenNotOwner);

    this.mockMvc.perform(deleteRequest)
      .andExpect(status().isForbidden())
      .andExpect(content().string("{\"message\":\"You are not allowed to: delete a post that is not yours\"}"));
  }

  @Test
  @DisplayName("shouldn't create post without token")
  public void createPostWithoutToken() throws Exception {
    Map<String, String> postPayload = Map.of("title", "test title", "content", "test content");
    RequestBuilder postRequest = post("/user/post")
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestHelpers.objectToJson(postPayload));
    this.mockMvc.perform(postRequest)
      .andExpect(status().isForbidden())
      .andExpect(content().string(""));
  }

  @Test
  @DisplayName("should list all posts")
  public void listAllUsers() throws Exception {
    this.makeRequest("/user", UserFixtures.validUserCreate, null, 201);
    Map<String, String> login = Map.of(
      "email", UserFixtures.validUserCreate.get("email"),
      "password", UserFixtures.validUserCreate.get("password")
    );
    String token = this.login(login);
    List<GenericJson> expectedPosts = new ArrayList<>();

    for (int index = 0; index < 5; index++) {
      Map<String, String> postPayload = Map.of("title", "test title" + index, "content", "test content");
      GenericJson post = this.makeRequest("/user/post", postPayload, token, 201);
      expectedPosts.add(post);
    }

    RequestBuilder request = get("/post")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer " + token);

    String json = this.mockMvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andReturn().getResponse().getContentAsString();

    List<GenericJson> response = this.jsonMapper.readValue(json, new TypeReference<>() {});

    assertEquals(expectedPosts, response);
  }

  @Test
  @DisplayName("should find post by id")
  public void findPostById() throws Exception {
    this.makeRequest("/user", UserFixtures.validUserCreate, null, 201);
    Map<String, String> login = Map.of(
      "email", UserFixtures.validUserCreate.get("email"),
      "password", UserFixtures.validUserCreate.get("password")
    );
    String token = this.login(login);

    Map<String, String> postPayload = Map.of("title", "test title", "content", "test content");
    GenericJson post = this.makeRequest("/user/post", postPayload, token, 201);

    RequestBuilder request = get("/post/" + post.get("id"))
      .header("Authorization", "Bearer " + token)
      .contentType(MediaType.APPLICATION_JSON);

    String json = this.mockMvc.perform(request)
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andReturn().getResponse().getContentAsString();

    GenericJson responsePost = this.jsonMapper.readValue(json, GenericJson.class);

    assertEquals(post, responsePost);
  }
}
