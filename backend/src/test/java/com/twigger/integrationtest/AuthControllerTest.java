package com.twigger.integrationtest;

import com.twigger.entity.User;
import com.twigger.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = GlobalTestConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    String token;
    String requestJson = "{\n" +
            "\t\"username\":\"test_user_1\",\n" +
            "\t\"password\":\"test_password_1\"\n" +
            "}";
    String withoutPassword = "{\n" +
            "\t\"username\":\"test\",\n" +
            "\t\"password\":\"\"\n" +
            "}";
    String withoutUsername = "{\n" +
            "\t\"username\":\"\",\n" +
            "\t\"password\":\"test\"\n" +
            "}";

    @Test
    public void testGetCurrentUserWhileUnauthorized() throws Exception {
        this.mockMvc.perform(get("/api/auth/current"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetCurrentUserWhileAuthorized() throws Exception {
        token = userService.signInUser(new User("test_user_1", "test_password_1"));
        this.mockMvc.perform(get("/api/auth/current")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer "+token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test_user_1")));
    }

    @Test
    public void testSignIn() throws Exception {
        this.mockMvc.perform(
                post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Token")));
    }

    @Test
    public void testSignInWithEmptyUsernameOrPassword() throws Exception {
        this.mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(withoutUsername))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid password or username.")));
        this.mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(withoutPassword))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid password or username.")));
    }

    @Test
    public void testSignUpWithEmptyUsernameOrPassword() throws Exception {
        this.mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(withoutUsername))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid password or username.")));
        this.mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(withoutPassword))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid password or username.")));
    }

    @Test
    public void testSignUpNewUser() throws Exception {
        String json = "{\n" +
                "\t\"username\":\"new_test_user\",\n" +
                "\t\"password\":\"test\"\n" +
                "}";
        this.mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Token")));
    }

    @Test
    public void testSignUpWithExistUser() throws Exception {
        this.mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("User exists.")));
    }
}
