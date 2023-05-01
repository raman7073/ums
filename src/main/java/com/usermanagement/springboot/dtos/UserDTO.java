package com.usermanagement.springboot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID uuid;
    @JsonProperty("user_name")
    @NotEmpty(message = "User Name should not be null or empty")
    private String userName;
    @NotEmpty(message = "Password should not be null or empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty("first_name")
    @NotEmpty(message = "First Name should not be null or empty")
    private String firstName;
    @JsonProperty("last_name")
    @NotEmpty(message = "Last Name should not be null or empty")
    private String lastName;
    @NotEmpty(message = "Role should not be null or empty")
    private String role;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("deleted_at")
    private Date deletedAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(userName, userDTO.userName) && Objects.equals(password, userDTO.password) && Objects.equals(firstName, userDTO.firstName) && Objects.equals(lastName, userDTO.lastName) && Objects.equals(role, userDTO.role) && Objects.equals(createdAt, userDTO.createdAt) && Objects.equals(updatedAt, userDTO.updatedAt) && Objects.equals(deletedAt, userDTO.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, userName, password, firstName, lastName, role, createdAt, updatedAt, deletedAt);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "uuid= '" + uuid + '\'' +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
