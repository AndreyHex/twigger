package com.twigger.controller;

import com.twigger.AbstractIntegrationTest;
import com.twigger.entity.User;
import com.twigger.service.MessageService;
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

public class MessageControllerTest  extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    private String token;
    private String message = "{\n" +
            "\t\"text\": \"message\"\n" +
            "}";

    @BeforeAll
    void initAll() {
        token = userService.signInUser(new User("message_test", "test"));
    }

    @Test
    public void testPostUnauthorizedMessage() throws Exception {
        this.mockMvc.perform(post("/api/message")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(message))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPostAuthorizedMessage() throws Exception {
        token = userService.signUpUser(new User("message_test", "test"));

        this.mockMvc.perform(post("/api/message")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer "+token)
                    .content(message))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAllMessages() throws Exception {
        this.mockMvc.perform(get("/api/message"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
