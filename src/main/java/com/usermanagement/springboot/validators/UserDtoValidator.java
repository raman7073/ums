package com.usermanagement.springboot.validators;

import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.exceptions.InvalidUserRequestBodyException;

import java.lang.reflect.Field;
import java.util.Objects;

public class UserDtoValidator {
    public static boolean isEmpty(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(obj) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                // log exception here
            }
        }
        return true;
    }

    public static boolean isValid(UserDTO userDTO) {
        if (isEmpty(userDTO)) {
            throw new InvalidUserRequestBodyException("Every field", "All fields are empty ");
        }
        if (userDTO.getUserName() == null || Objects.equals(userDTO.getUserName(), "")) {
            throw new InvalidUserRequestBodyException("User Name", "Either not Provided OR User Name is empty or null ");
        }
        if (userDTO.getFirstName() == null || Objects.equals(userDTO.getFirstName(), "")) {
            throw new InvalidUserRequestBodyException("First Name", "Either not Provided OR First Name is empty or null");
        }
        if (userDTO.getLastName() == null || Objects.equals(userDTO.getLastName(), "")) {
            throw new InvalidUserRequestBodyException("Last Name", "Either not Provided OR Last Name is empty or null ");
        }
        if (userDTO.getRole() == null || Objects.equals(userDTO.getRole(), "")) {
            throw new InvalidUserRequestBodyException("Role", "Either not Provided OR Role is empty or null ");
        }
        return true;

    }
}
