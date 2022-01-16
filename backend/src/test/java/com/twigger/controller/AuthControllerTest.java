package com.twigger.controller;

import com.twigger.entity.Role;
import com.twigger.entity.User;
import com.twigger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void initData() {
        User user = new User();
        user.setUsername("test");
        user.setPassword(bCryptPasswordEncoder.encode("test"));
        user.setEmail("test@test");
        user.setStatus(true);
        user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        userRepository.saveAndFlush(user);
    }

    @Test
    public void unauthorizedCurrentUserTest() throws Exception {
        this.mockMvc.perform(get("/api/auth/current"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    String requestJson = "{\n" +
            "\t\"username\":\"test\",\n" +
            "\t\"password\":\"test\"\n" +
            "}";
    @Test
    public void signupTest() throws Exception {
        this.mockMvc.perform(
                post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Token")));
    }
}
