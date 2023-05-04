package com.usermanagement.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidUserRequestBodyException extends RuntimeException {
    private final String fieldName;
    private final String reason;

    public InvalidUserRequestBodyException(String fieldName, String reason) {

        super(String.format("  %s : Not Found, reason : %s", fieldName, reason));
        this.fieldName = fieldName;
        this.reason = reason;
    }
}
