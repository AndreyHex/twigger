package com.twigger.controller;

import com.twigger.entity.User;
import com.twigger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public ResponseEntity<?> currentUser(Principal principal) {
        return ResponseEntity.ok(principal);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.signUpUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> registration(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.signInUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

}
