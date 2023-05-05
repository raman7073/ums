package com.ums.springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ums.springboot.entities.User;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class UserDTO implements Converter<UserDTO, User> {

    private UUID userId;
    @NotEmpty(message = "User Name should not be null or empty")
    private String userName;
    @NotEmpty(message = "Password should not be null or empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotEmpty(message = "First Name should not be null or empty")
    private String firstName;
    @NotEmpty(message = "Last Name should not be null or empty")
    private String lastName;
    @NotEmpty(message = "Role should not be null or empty")
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Override
    public User convert(UserDTO userDTO) {

        return User.builder().userName(
                userDTO.getUserName()).
                password(userDTO.getPassword()).
                firstName(userDTO.getFirstName()).
                lastName(userDTO.getLastName()).
                role(userDTO.getRole()).
                build();
    }
}