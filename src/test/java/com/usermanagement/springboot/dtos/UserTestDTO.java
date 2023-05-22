package com.usermanagement.springboot.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.usermanagement.springboot.entities.User;
import lombok.*;
import org.springframework.boot.jackson.JsonMixin;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.usermanagement.springboot.common.Constants.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonMixin(UserDTO.class)
public class UserTestDTO implements Converter<User, UserTestDTO> {

    private UUID userId;

    @NotEmpty(message = INVALID_USERNAME)
    private String username;

    @NotEmpty(message = INVALID_PASSWORD)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
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
    public UserTestDTO convert(User user) {

        this.setUserId(user.getUserId());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setUserId(user.getUserId());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setRole(user.getRole());
        this.setCreatedAt(user.getCreatedAt());
        this.setUpdatedAt(user.getUpdatedAt());
        this.setDeletedAt(user.getDeletedAt());
        return this;
    }


}

