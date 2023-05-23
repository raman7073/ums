package com.usermanagement.springboot.exceptionhandler;

import com.usermanagement.springboot.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.usermanagement.springboot.common.Constants.*;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                RESOURCE_NOT_FOUND
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNameAlreadyExistException.class)
    public ResponseEntity<ErrorDetails> handleUserNameAlreadyExistException(
            UserNameAlreadyExistException exception,
            WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                USER_NAME_ALREADY_EXIST
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<ErrorDetails> handleInvalidUsernameOrPasswordException(
            InvalidUsernameOrPasswordException exception,
            WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                INVALID_USERNAME_OR_PASSWORD
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllOtherException(Exception exception,
                                                                WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                               WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();

        errorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}
