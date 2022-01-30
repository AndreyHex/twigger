package com.twigger.service;

import com.twigger.entity.Message;
import com.twigger.entity.User;
import com.twigger.exception.BadRequestException;
import com.twigger.exception.NotFoundException;
import com.twigger.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;

    public List<Message> findAll(Instant time) {
        return messageRepository.findTop50ByPostDateBeforeOrderByPostDateDesc(time);
    }

    public List<Message> findAllByUsername(String username, Instant time) {
        if(username != null)
            userService.loadUserByUsername(username);
        else throw new BadRequestException("Invalid username.");
        return messageRepository.findAllByUser_UsernameAndPostDateBeforeOrderByPostDateDesc(username, time);
    }

    public Message save(Message message) throws BadRequestException {
        if(message.getText().isEmpty()) throw new BadRequestException("Message is empty.");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        message.setUser(user);
        message.setPostDate(Instant.now());
        message.setPublicId(getNewPublicId());
        return messageRepository.save(message);
    }

    public Message findByPublicId(String publicId) {
        return messageRepository.findByPublicId(publicId).orElseThrow(() -> new NotFoundException("Message doesnt exist."));
    }

    private String getNewPublicId() {
        String publicId;
        do {
            publicId = generateHexString();
        } while (messageRepository.existsByPublicId(publicId));
        return publicId;
    }

    private String generateHexString() {
        return Long.toHexString(ThreadLocalRandom.current().nextLong());
    }
}
