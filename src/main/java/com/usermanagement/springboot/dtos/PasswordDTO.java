package com.usermanagement.springboot.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO {
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
}
