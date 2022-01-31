package com.twigger.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.twigger.entity.Message;
import com.twigger.entity.View;
import com.twigger.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    @JsonView(View.FullMessage.class)
    public ResponseEntity findAll(@RequestParam Optional<String> before_datetime) { //2020-12-10T20:00:10+02:00 format
        Instant time = Instant.now();
        if(before_datetime.isPresent()) {
            try {
                time = OffsetDateTime.parse(before_datetime.get()).toInstant();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.ok(messageService.findAll(time));
    }

    @GetMapping("/by/{username}")
    @JsonView(View.FullMessage.class)
    public ResponseEntity findAllByUsername(@PathVariable("username") String username,
                                            @RequestParam Optional<String> before_datetime) {
        Instant time = Instant.now();
        if(before_datetime.isPresent()) {
            try {
                time = OffsetDateTime.parse(before_datetime.get()).toInstant();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.ok(messageService.findAllByUsername(username, time));
    }

    @GetMapping("/id/{publicId}")
    @JsonView({View.FullMessage.class})
    public ResponseEntity findByPublicId(@PathVariable("publicId") String publicId) {
        return ResponseEntity.ok(messageService.findByPublicId(publicId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @JsonView(View.FullMessage.class)
    public ResponseEntity create(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.save(message));
    }

}
