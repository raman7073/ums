package com.usermanagement.springboot.dtos;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
public class LoginDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
