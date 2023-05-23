package com.usermanagement.springboot.services;


import com.usermanagement.springboot.dtos.AuthResponseDTO;
import com.usermanagement.springboot.dtos.LoginDTO;
import com.usermanagement.springboot.exceptions.InvalidUsernameOrPasswordException;
import com.usermanagement.springboot.security.JwtTokenUtil;
import com.usermanagement.springboot.services.Impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void testLogin_givenValidCredentials_whenLogin_thenReturnAuthResponseDTO() {

        /* given */
        String username = "tester";
        String password = "password";
        String accessToken = "someAccessToken";
        LoginDTO loginDTO = new LoginDTO(username, password);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        given(authenticationManager.authenticate(authenticationToken)).willReturn(authentication);
        given(jwtTokenUtil.generateToken(username)).willReturn(accessToken);

        /* when */
        AuthResponseDTO authResponseDTO = authService.login(loginDTO);

        /* then */
        assertNotNull(authResponseDTO);
        assertEquals(accessToken, authResponseDTO.getAccessToken());
        verify(authenticationManager, times(1))
                .authenticate(authenticationToken);
        verify(jwtTokenUtil, times(1)).generateToken(username);
    }

    @Test
    public void testLogin_givenInvalidCredentials_whenLogin_thenThrowInvalidUsernameOrPasswordException() {

        /* given */
        String username = "tester";
        String password = "wrongPassword";
        LoginDTO loginDTO = new LoginDTO(username, password);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        given(authenticationManager.authenticate(authenticationToken))
                .willThrow(BadCredentialsException.class);

        /* when */
        assertThrows(InvalidUsernameOrPasswordException.class,
                () -> authService.login(loginDTO)
        );
        /* then */
        verify(authenticationManager, times(1)).authenticate(authenticationToken);
        verify(jwtTokenUtil, never()).generateToken(anyString());
    }
}


