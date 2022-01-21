package com.twigger.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.twigger.entity.User;
import com.twigger.entity.View;
import com.twigger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    @JsonView(View.Username.class)
    public ResponseEntity<?> currentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signin")
    @JsonView(View.UserWithPassword.class)
    public ResponseEntity<?> login(@RequestBody User user) {
        return ResponseEntity.ok(Map.entry("Token", userService.signInUser(user)));
    }

    @PostMapping("/signup")
    @JsonView(View.UserWithPassword.class)
    public ResponseEntity<?> registration(@RequestBody User user) {
        return ResponseEntity.ok(Map.entry("Token", userService.signUpUser(user)));
    }

}
