package com.twigger.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.twigger.entity.Message;
import com.twigger.entity.View;
import com.twigger.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    @JsonView(View.MessageWithUser.class)
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(messageService.getAll());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.save(message));
    }

}
