package com.twigger.service;

import com.twigger.entity.Message;
import com.twigger.entity.User;
import com.twigger.exception.BadRequestException;
import com.twigger.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;

    public List<Message> findAll() {
        return messageRepository.findTop50ByOrderByPostDateDesc();
    }

    public List<Message> findAllByUsername(String username) {
        if(username != null)
            userService.loadUserByUsername(username);
        else throw new BadRequestException("Invalid username.");
        return messageRepository.findAllByUser_UsernameOrderByPostDateDesc(username);
    }

    public Message save(Message message) throws BadRequestException {
        if(message.getText().isEmpty()) throw new BadRequestException("Message is empty.");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        message.setUser(user);
        message.setPostDate(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
