package com.usermanagement.springboot.services;

import com.usermanagement.springboot.dtos.AuthResponseDTO;
import com.usermanagement.springboot.dtos.LoginDTO;
import com.usermanagement.springboot.exceptions.InvalidUsernameOrPasswordException;
import com.usermanagement.springboot.security.JwtTokenUtil;
import com.usermanagement.springboot.services.Impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtTokenUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;
    private LoginDTO loginDTO;

    @BeforeEach
    public void setup() {
        loginDTO = new LoginDTO("tester", "password");
    }

    @Test
    public void testLogin_givenValidCredentials_thenReturnAuthResponseDTO() {

        /* given */
        String accessToken = "someAccessToken";
        when(authManager
                .authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtUtil
                .generateToken(loginDTO.getUsername()))
                .thenReturn(accessToken);

        /* when */
        AuthResponseDTO authResponseDTO = authService.login(loginDTO);

        /* then */
        assertNotNull(authResponseDTO);
        assertEquals(accessToken, authResponseDTO.getAccessToken());
    }

    @Test
    public void testLogin_givenInvalidCredentials_thenThrowInvalidUsernameOrPasswordException() {

        /* given */
        when(authManager
                .authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        /* when */
        assertThrows(InvalidUsernameOrPasswordException.class,
                () -> authService.login(loginDTO)
        );

        /* then */
        verify(jwtUtil, never()).generateToken(loginDTO.getUsername());
    }
}
