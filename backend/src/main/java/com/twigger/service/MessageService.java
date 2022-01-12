package com.twigger.service;

import com.twigger.entity.Message;
import com.twigger.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    public Message save(Message message) {
        message.setPostDate(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
