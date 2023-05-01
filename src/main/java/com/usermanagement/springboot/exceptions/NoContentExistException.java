package com.usermanagement.springboot.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NO_CONTENT)
public class NoContentExistException extends RuntimeException {
    private final String message;
    public NoContentExistException(String message) {

        super(message);
        this.message=message;

    }
}
