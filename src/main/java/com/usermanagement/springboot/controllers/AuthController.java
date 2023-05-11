package com.usermanagement.springboot.controllers;

import com.usermanagement.springboot.dtos.AuthRequestDTO;
import com.usermanagement.springboot.dtos.AuthResponseDTO;
import com.usermanagement.springboot.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("v1/auth")
public class AuthController {

    private AuthenticationManager authManager;
    private JwtTokenUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDTO authRequestDTO) {

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getUsername(), authRequestDTO.getPassword()
                    );
            Authentication authentication = authManager.authenticate(usernamePasswordAuthenticationToken);
            String accessToken = jwtUtil.generateToken(authRequestDTO.getUsername());
            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setAccessToken(accessToken);
            return ResponseEntity.ok(authResponseDTO);

        } catch (BadCredentialsException exception) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username Or Password");
        }

    }
}