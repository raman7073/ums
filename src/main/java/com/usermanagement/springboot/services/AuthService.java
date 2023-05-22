package com.usermanagement.springboot.services;

import com.usermanagement.springboot.dtos.AuthResponseDTO;
import com.usermanagement.springboot.dtos.LoginDTO;

public interface AuthService {
    AuthResponseDTO login(LoginDTO loginDTO);
}
