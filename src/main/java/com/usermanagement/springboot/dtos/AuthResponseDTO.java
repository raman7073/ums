package com.usermanagement.springboot.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.usermanagement.springboot.common.Constants.BEARER;

@Getter
@Setter
@ToString
public class AuthResponseDTO {

    private String accessToken;
    private String tokenType = BEARER;

}
