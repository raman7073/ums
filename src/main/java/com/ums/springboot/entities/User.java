package com.ums.springboot.entities;

import com.ums.springboot.dtos.UserDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class User implements Converter<User, UserDTO> {

    private UUID userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted = Boolean.FALSE;
    private LocalDateTime deletedAt;

    @Override
    public UserDTO convert(User user) {

        return UserDTO.builder().userName(
                user.getUserName()).
                userId(user.getUserId()).
                firstName(user.getFirstName()).
                lastName(user.getLastName()).
                role(user.getRole()).
                createdAt(user.getCreatedAt()).
                updatedAt(user.getUpdatedAt()).
                build();
    }
}
