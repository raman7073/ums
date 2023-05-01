package com.usermanagement.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserNameAlreadyExistException extends RuntimeException {
    private final String message;
    public UserNameAlreadyExistException(String message) {
        super(message);
        this.message=message;
    }
}
