package com.usermanagement.springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.usermanagement.springboot.entities.User;
import lombok.*;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.usermanagement.springboot.common.Constants.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Converter<User, UserDTO> {

    private UUID userId;

    @NotBlank(message = INVALID_USERNAME)
    private String username;

    @NotBlank(message = INVALID_PASSWORD)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = INVALID_FIRST_NAME)
    private String firstName;

    @NotBlank(message = INVALID_LAST_NAME)
    private String lastName;

    @NotBlank(message = INVALID_ROLE)
    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public UserDTO convert(User user) {

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
