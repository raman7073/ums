package com.usermanagement.springboot.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AuthRequestDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
