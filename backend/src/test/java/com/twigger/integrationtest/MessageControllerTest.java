package com.twigger.integrationtest;

import com.twigger.entity.Message;
import com.twigger.entity.User;
import com.twigger.repository.MessageRepository;
import com.twigger.service.UserService;
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
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageRepository messageRepository;

    private String token;
    private String message = "{\n" +
            "\t\"text\": \"message\"\n" +
            "}";

    @Test
    public void testPostMessageWhileUnauthorized() throws Exception {
        this.mockMvc.perform(post("/api/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(message))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPostMessageWhileAuthorized() throws Exception {
        token = userService.signInUser(new User("test_user_1", "test_password_1"));
        this.mockMvc.perform(post("/api/messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer "+token)
                    .content(message))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPostEmptyMessage() throws Exception {
        token = userService.signInUser(new User("test_user_1", "test_password_1"));
        String emptyMessage = "{\n" +
                "\t\"text\": \"\"\n" +
                "}";
        this.mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token)
                .content(emptyMessage))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindAllMessages() throws Exception {
        this.mockMvc.perform(get("/api/messages"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAllMessagesByUsername() throws Exception {
        this.mockMvc.perform(get("/api/messages/by/test_user_1"))
                .andDo(print())
                .andExpect(status().isOk())
        .andExpect(content().string(containsString("test_user_1")));
    }

    @Test
    public void testFindMessageByPublicId() throws Exception {
        Message msg = new Message();
        msg.setText("Text");
        msg.setPublicId("tttttt");
        msg.setUser((User) userService.loadUserByUsername("test_user_1"));
        messageRepository.saveAndFlush(msg);

        this.mockMvc.perform(get("/api/messages/id/"+msg.getPublicId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(msg.getPublicId())));
    }

    @Test
    public void testFindMessageByPublicIdWhileMessageNotExists() throws Exception {
        this.mockMvc.perform(get("/api/messages/id/"+"00fffff"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
