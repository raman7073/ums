package com.usermanagement.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usermanagement.springboot.dtos.AuthResponseDTO;
import com.usermanagement.springboot.dtos.LoginDTO;
import com.usermanagement.springboot.security.JwtTokenFilter;
import com.usermanagement.springboot.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTests {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testLogin_whenLogin_thenReturnAuthResponseDTO() throws Exception {

        /* given */
        LoginDTO loginDTO = new LoginDTO("username", "password");
        AuthResponseDTO expectedResponseDTO = new AuthResponseDTO();
        expectedResponseDTO.setAccessToken("token");
        given(authService.login(any(LoginDTO.class))).willReturn(expectedResponseDTO);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }


    @Test
    public void testLogin_givenInvalidCredentials_whenLogin_thenReturn401() throws Exception {

        /* given */
        LoginDTO loginDTO = new LoginDTO("tester", "wrongPassword");
        given(authService.login(loginDTO)).willThrow(BadCredentialsException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andExpect(MockMvcResultMatchers
                .status().isUnauthorized()).andDo(print());
    }

    @Test
    public void testLogin_givenInvalidArgs_whenLogin_thenReturn400() throws Exception {

        /* given */
        LoginDTO loginDTO = new LoginDTO("", "");
        given(authService.login(loginDTO)).willThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andExpect(MockMvcResultMatchers
                .status().isBadRequest()).andDo(print());
    }
}

