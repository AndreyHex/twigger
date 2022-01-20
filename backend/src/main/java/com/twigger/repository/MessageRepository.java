package com.twigger.repository;

import com.twigger.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAll();

    List<Message> findAllByUser_UsernameOrderByPostDateAsc(String username);

    List<Message> findTop50ByOrderByPostDate();



}
