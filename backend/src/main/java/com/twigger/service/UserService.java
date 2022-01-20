package com.twigger.service;

import com.twigger.entity.Role;
import com.twigger.entity.User;
import com.twigger.exception.InvalidPasswordOrUsername;
import com.twigger.exception.UserExistsException;
import com.twigger.exception.UserNotFoundException;
import com.twigger.repository.UserRepository;
import com.twigger.utils.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    public String signInUser(User user) throws InvalidPasswordOrUsername, UserExistsException {
        if(userRepository.findUserByUsername(user.getUsername()).isPresent())
            throw new UserExistsException("User exists.");
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty()) throw new InvalidPasswordOrUsername("Incorrect username or password.");
        String rawPassword = user.getPassword();
        save(user);
        return authenticateUser(user.getUsername(), rawPassword);
    }

    public String signUpUser(User user) throws UserNotFoundException {
        return authenticateUser(user.getUsername(), user.getPassword());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private User save(User user) {
        user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setStatus(true);
        user.setRegistrationDate(LocalDateTime.now());
        return userRepository.saveAndFlush(user);
    }

    private String authenticateUser(String username, String password) throws UserNotFoundException {
        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("User not found.", e);
        }
        return jwtUtils.generateJwtToken(auth);
    }

    private void updateLastLoginDate(User user) {
        user.setLastLoginDate(LocalDateTime.now());
    }
}
