package com.usermanagement.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidUsernameOrPasswordException  extends RuntimeException{
    private final String message;

    public InvalidUsernameOrPasswordException(String message,Throwable cause) {
        super(message,cause);
        this.message = message;
    }
}
