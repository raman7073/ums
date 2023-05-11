package com.usermanagement.springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usermanagement.springboot.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class UserDTO implements Converter<UserDTO, User> {

    private UUID userId;

    @NotEmpty(message = "User Name should not be null or empty")
    private String username;

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
        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setRole(userDTO.getRole());
        return user;
    }
}
