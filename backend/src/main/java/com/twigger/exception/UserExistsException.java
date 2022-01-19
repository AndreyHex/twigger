package com.twigger.exception;

public class UserExistsException extends BadRequestException{
    public UserExistsException(String s) {
        super(s);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
