package com.usermanagement.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usermanagement.springboot.daos.UserDAO;
import com.usermanagement.springboot.dtos.AuthResponseDTO;
import com.usermanagement.springboot.dtos.LoginDTO;
import com.usermanagement.springboot.exceptions.InvalidUsernameOrPasswordException;
import com.usermanagement.springboot.security.JwtTokenFilter;
import com.usermanagement.springboot.services.AuthService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @MockBean
    private UserDAO userDAO;
    @MockBean
    private AuthenticationManager authManager;
    private LoginDTO loginDTO;

    @BeforeEach
    public void setup() {
        loginDTO = new LoginDTO("username", "password");
    }

    @Test
    public void testLogin_whenLogin_thenReturnAuthResponseDTO() throws Exception {

        /* given */
        AuthResponseDTO expectedResponseDTO = new AuthResponseDTO();
        expectedResponseDTO.setAccessToken("token");
        expectedResponseDTO.setTokenType("Bearer");
        when(authService.login(any(LoginDTO.class))).thenReturn(expectedResponseDTO);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken",
                        CoreMatchers.is(expectedResponseDTO.getAccessToken())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tokenType",
                        CoreMatchers.is(expectedResponseDTO.getTokenType())));
    }

    @Test
    public void testLogin_givenInvalidCredentials_whenLogin_thenReturn401() throws Exception {

        /* given */
        when(authService.login(any(LoginDTO.class)))
                .thenThrow(InvalidUsernameOrPasswordException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isUnauthorized());
    }

    @Test
    public void testLogin_givenUsernameAsNull_whenLogin_thenReturn400() throws Exception {

        /* given */
        loginDTO.setUsername(null);
        when(authService.login(loginDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testLogin_givenInvalidUsernameUsername_whenLogin_thenReturn400(String Username) throws Exception {

        /* given */
        loginDTO.setUsername(Username);
        when(authService.login(loginDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin_givenPasswordAsNull_whenLogin_thenReturn400() throws Exception {

        /* given */
        loginDTO.setPassword(null);
        when(authService.login(loginDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testLogin_givenInvalidPassword_whenLogin_thenReturn400(String password) throws Exception {

        /* given */
        loginDTO.setPassword(password);
        when(authService.login(loginDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin_givenPasswordAsSpace_whenLogin_thenReturn400() throws Exception {

        /* given */
        loginDTO.setPassword("       ");
        when(authService.login(loginDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin_givenInvalidArgs_whenLogin_thenReturn400() throws Exception {

        /* given */
        loginDTO.setUsername(null);
        loginDTO.setPassword("       ");
        when(authService.login(loginDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(loginDTO)));

        /* then */
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }
}
