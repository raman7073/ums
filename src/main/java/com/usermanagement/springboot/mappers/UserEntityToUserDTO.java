package com.usermanagement.springboot.mappers;

import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToUserDTO implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User user) {
        return UserDTO.builder().userName(user.getUserName()).uuid(user.getUuid()).
                firstName(user.getFirstName()).lastName(user.getLastName()).
                role(user.getRole()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();

    }

}
