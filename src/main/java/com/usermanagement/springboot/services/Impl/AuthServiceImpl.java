package com.usermanagement.springboot.services.Impl;

import com.usermanagement.springboot.dtos.AuthResponseDTO;
import com.usermanagement.springboot.dtos.LoginDTO;
import com.usermanagement.springboot.exceptions.InvalidUsernameOrPasswordException;
import com.usermanagement.springboot.security.JwtTokenUtil;
import com.usermanagement.springboot.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtTokenUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    );
            authManager.authenticate(usernamePasswordAuthenticationToken);
            String accessToken = jwtUtil.generateToken(loginDTO.getUsername());
            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setAccessToken(accessToken);
            return authResponseDTO;
        } catch (BadCredentialsException exception) {

            throw new InvalidUsernameOrPasswordException(
                    "INVALID_USERNAME_OR_PASSWORD",
                    exception.getCause());
        }
    }
}
