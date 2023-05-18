package com.usermanagement.springboot.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PasswordDTO {
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
}
