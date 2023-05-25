package com.usermanagement.springboot.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
