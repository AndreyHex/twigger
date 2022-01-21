package com.twigger.controller;

import com.twigger.AbstractIntegrationTest;
import com.twigger.entity.Message;
import com.twigger.entity.User;
import com.twigger.repository.MessageRepository;
import com.twigger.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageControllerTest  extends AbstractIntegrationTest {

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

    @BeforeAll
    void initAll() {
        token = userService.signUpUser(new User("message_test", "test"));
        User user = (User) userService.loadUserByUsername("message_test");
        messageRepository.saveAllAndFlush(
                IntStream.range(1, 60)
                        .mapToObj(a -> createMessage(user, "Message by message_test #"+a))
                        .collect(Collectors.toList())
        );
    }

    private Message createMessage(User user, String text) {
        Message newMsg =  new Message();
        newMsg.setPostDate(LocalDateTime.now());
        newMsg.setUser(user);
        newMsg.setText(text);
        return newMsg;
    }

    @Test
    public void testPostMessageWhileUnauthorized() throws Exception {
        this.mockMvc.perform(post("/api/message")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(message))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPostMessageWhileAuthorized() throws Exception {
        this.mockMvc.perform(post("/api/message")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer "+token)
                    .content(message))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPostEmptyMessage() throws Exception {
        String emptyMessage = "{\n" +
                "\t\"text\": \"\"\n" +
                "}";
        this.mockMvc.perform(post("/api/message")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token)
                .content(emptyMessage))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindAllMessages() throws Exception {
        this.mockMvc.perform(get("/api/message"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAllMessagesByUsername() throws Exception {
        String user = "{\"username\":\"message_test\"}";
        this.mockMvc.perform(get("/api/message")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(user))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
