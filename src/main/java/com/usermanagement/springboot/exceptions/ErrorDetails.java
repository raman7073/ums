package com.usermanagement.springboot.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String message;
    private String path;
    private String errorCode;
}
