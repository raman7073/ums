package com.usermanagement.springboot.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
