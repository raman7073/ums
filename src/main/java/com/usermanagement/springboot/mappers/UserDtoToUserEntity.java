package com.usermanagement.springboot.mappers;

import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserEntity implements Converter<UserDTO, User> {
    @Override
    public User convert(UserDTO userDTO) {
        return User.builder().userName(userDTO.getUserName()).password(userDTO.getPassword()).
                firstName(userDTO.getFirstName()).lastName(userDTO.getLastName()).
                role(userDTO.getRole()).build();
    }
}
