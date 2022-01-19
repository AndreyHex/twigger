package com.twigger.controller;

import com.twigger.AbstractIntegrationTest;
import com.twigger.entity.User;
import com.twigger.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    String token;
    String requestJson = "{\n" +
            "\t\"username\":\"auth_test\",\n" +
            "\t\"password\":\"test\"\n" +
            "}";

    @BeforeAll
    void initAll() {
        token = userService.signInUser(new User("auth_test", "test"));
    }

    @Test
    public void testGetUnauthorizedCurrentUser() throws Exception {
        this.mockMvc.perform(get("/api/auth/current"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAuthorizedCurrentUser() throws Exception {
        this.mockMvc.perform(get("/api/auth/current")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer "+token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("auth_test")));
    }

    @Test
    public void testSignUp() throws Exception {
        this.mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Token")));
    }
}
