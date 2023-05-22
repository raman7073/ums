package com.usermanagement.springboot.dtos;

import lombok.Getter;
import lombok.Setter;

import static com.usermanagement.springboot.common.Constants.BEARER;

@Getter
@Setter
public class AuthResponseDTO {

    private String accessToken;
    private String tokenType = BEARER;

}
