package com.twigger.service;

import com.twigger.entity.Role;
import com.twigger.entity.User;
import com.twigger.exception.BadRequestException;
import com.twigger.exception.InvalidPasswordOrUsername;
import com.twigger.exception.NotFoundException;
import com.twigger.exception.UserNotFoundException;
import com.twigger.repository.UserRepository;
import com.twigger.utils.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public String signInUser(User user) throws InvalidPasswordOrUsername {
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty()) throw new InvalidPasswordOrUsername("Incorrect username or password.");
        String rawPassword = user.getPassword();
        user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setStatus(true);
        user.setRegistrationDate(LocalDateTime.now());
        userRepository.saveAndFlush(user);
        return authenticateUser(user);
    }

    public String signUpUser(User user) throws UserNotFoundException {
        return authenticateUser(user);
    }

    private String authenticateUser(User user) throws UserNotFoundException {
        Authentication auth = null;
        System.out.println("In auth: " + userRepository.findUserByUsername(user.getUsername()).orElse(new User()));
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("User not found.");
        }
        return jwtUtils.generateJwtToken(auth);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
