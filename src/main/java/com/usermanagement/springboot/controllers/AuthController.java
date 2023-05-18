package com.usermanagement.springboot.controllers;

import com.usermanagement.springboot.dtos.AuthResponseDTO;
import com.usermanagement.springboot.dtos.LoginDTO;
import com.usermanagement.springboot.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.usermanagement.springboot.common.Constants.V1_AUTH;
import static com.usermanagement.springboot.common.Constants.LOGIN;

@RestController
@RequestMapping(V1_AUTH)
public class AuthController {
   @Autowired
   private AuthService authService;
    @PostMapping(LOGIN)
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {

        AuthResponseDTO authResponseDTO =authService.login(loginDTO);
        return ResponseEntity.ok(authResponseDTO);
    }
}