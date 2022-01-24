package com.twigger.controller;

import com.twigger.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody Map.Entry<String, String> notFound(NotFoundException e) {
        System.out.println(e.getMessage()); // need proper logging
        return Map.entry("Error", "Not found.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public @ResponseBody Map.Entry<String, String> userNotFound(UserNotFoundException e) {
        e.getCause().printStackTrace();
        System.out.println(e.getMessage()); // need proper logging
        return Map.entry("Error", "User not found.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordOrUsername.class)
    public @ResponseBody Map.Entry<String, String> invalidPasswordOrUsername(InvalidPasswordOrUsername e) {
        System.out.println(e.getMessage());
        return Map.entry("Error", "Invalid password or username.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserExistsException.class)
    public @ResponseBody Map.Entry<String, String> userExists(UserExistsException e) {
        System.out.println(e.getMessage());
        return Map.entry("Error", "User exists.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody Map.Entry<String, String> badRequest(BadRequestException e) {
        System.out.println(e.getMessage());
        return Map.entry("Error", "Bad request.");
    }

}
