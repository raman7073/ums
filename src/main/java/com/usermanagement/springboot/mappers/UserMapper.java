package com.usermanagement.springboot.mappers;

import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.entities.User;

public class UserMapper {
    public static User convertUserDtoToEntity(UserDTO userDTO) {
        return User.builder().userName(userDTO.getUserName()).
                firstName(userDTO.getFirstName()).lastName(userDTO.getLastName()).
                role(userDTO.getRole()).build();

    }

    public static UserDTO convertUserEntityToDto(User user) {
        return UserDTO.builder().userName(user.getUserName()).
                firstName(user.getFirstName()).lastName(user.getLastName()).
                role(user.getRole()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();

    }
}
