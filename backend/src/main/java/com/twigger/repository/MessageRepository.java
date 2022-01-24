package com.twigger.repository;

import com.twigger.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAll();

    List<Message> findAllByUser_UsernameOrderByPostDateDesc(String username);

    List<Message> findTop50ByOrderByPostDateDesc();

    boolean existsByPublicId(String publicId);

    Optional<Message> findByPublicId(String publicId);
}
