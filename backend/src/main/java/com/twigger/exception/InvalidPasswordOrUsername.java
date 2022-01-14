package com.twigger.exception;

public class InvalidPasswordOrUsername extends BadRequestException{
    public InvalidPasswordOrUsername(String s) {
        super(s);
    }
}
