package com.usermanagement.springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usermanagement.springboot.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.usermanagement.springboot.common.Constants.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDTO implements Converter<User, UserDTO> {

    private UUID userId;

    @NotEmpty(message = INVALID_USERNAME)
    private String username;

    @NotEmpty(message = INVALID_PASSWORD)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotEmpty(message = INVALID_FIRST_NAME)
    private String firstName;

    @NotEmpty(message = INVALID_LAST_NAME)
    private String lastName;

    @NotEmpty(message = INVALID_ROLE)
    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public UserDTO convert(User user) {
        return UserDTO.builder().username(user.getUsername()).
                userId(user.getUserId()).
                firstName(user.getFirstName()).
                lastName(user.getLastName()).
                role(user.getRole()).
                createdAt(user.getCreatedAt()).
                updatedAt(user.getUpdatedAt()).build();
    }
}
