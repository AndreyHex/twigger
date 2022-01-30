package com.twigger.repository;

import com.twigger.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAll();

    List<Message> findAllByUser_UsernameAndPostDateBeforeOrderByPostDateDesc(String username, Instant time);

    boolean existsByPublicId(String publicId);

    Optional<Message> findByPublicId(String publicId);

    List<Message> findTop50ByPostDateBeforeOrderByPostDateDesc(Instant time);
}
