package com.twigger.service;

import com.twigger.entity.Role;
import com.twigger.entity.User;
import com.twigger.repository.UserRepository;
import com.twigger.utils.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUsername(username);
    }

    public String signInUser(User user) throws Exception {
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty()) throw new Exception("Pass or name");
        user.setRoles(Collections.singleton(new Role(2L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setStatus(true);
        User newUser = userRepository.save(user);
        String token = authenticateUser(newUser);
        return token;
    }

    public String signUpUser(User user) throws Exception {
        String token = authenticateUser(user);
        return token;
    }

    private String authenticateUser(User user) throws Exception {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(!auth.isAuthenticated()) throw new Exception("Authentication error.");
        SecurityContextHolder.getContext().setAuthentication(auth);
        return jwtUtils.generateJwtToken(auth);
    }

}
