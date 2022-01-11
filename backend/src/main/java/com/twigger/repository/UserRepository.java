package com.twigger.repository;

import com.twigger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUsername(String username);

}
