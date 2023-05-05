package com.ums.springboot.exceptions;

public class UserNameAlreadyExistException extends RuntimeException {
    private final String message;

    public UserNameAlreadyExistException(String message) {

        super(message);
        this.message = message;
    }
}
