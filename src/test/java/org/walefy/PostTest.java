package org.walefy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
}
