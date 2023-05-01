package com.usermanagement.springboot.exceptions;

public class InvalidUserRequestBodyException extends RuntimeException {
    private final String fieldName;
    private final String reason;

    public InvalidUserRequestBodyException(String fieldName, String reason) {
        super(String.format("  %s : Not Found, reason : %s", fieldName, reason));
        this.fieldName = fieldName;
        this.reason = reason;
    }
}
