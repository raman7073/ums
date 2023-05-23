package com.usermanagement.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usermanagement.springboot.daos.UserDAO;
import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.exceptions.ResourceNotFoundException;
import com.usermanagement.springboot.exceptions.UserNameAlreadyExistException;
import com.usermanagement.springboot.security.JwtTokenFilter;
import com.usermanagement.springboot.services.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenFilter jwtTokenFilter;

    @MockBean
    private UserDAO userDAO;
    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    public void setup() {

        userDTO = new UserDTO();
        userDTO.setUsername("nial");
        userDTO.setPassword("admin123");
        userDTO.setFirstName("Raman");
        userDTO.setLastName("Mehta");
        userDTO.setRole("Admin");
    }

    @Test
    public void testCreateUser_whenCreateUser_thenReturnSavedUser() throws Exception {

        /* given */
        given(userService.createUser(ArgumentMatchers.any(UserDTO.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andExpect(MockMvcResultMatchers
                        .status().isCreated())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username",
                        CoreMatchers.is(userDTO.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",
                        CoreMatchers.is(userDTO.getPassword())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(userDTO.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(userDTO.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role",
                        CoreMatchers.is(userDTO.getRole())));
    }

    @Test
    public void testCreateUser_givenExistingUserName_whenCreateUser_thenReturn409() throws Exception {

        /* given */
        given(userService.createUser(userDTO)).willThrow(UserNameAlreadyExistException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andExpect(MockMvcResultMatchers
                .status().isConflict());
    }

    @Test
    public void testCreateUser_givenInvalidArgs_whenCreateUser_thenThrowsException() throws Exception {

        /* given */
        userDTO.setUsername("");
        given(userService.createUser(userDTO)).willThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andExpect(MockMvcResultMatchers
                .status().isBadRequest());
    }

    @Test
    public void testGetAllUser_whenGetAllUsers_thenReturnUserDTOList()
            throws Exception {

        /* given */
        List<UserDTO> userDTOList = new ArrayList<>();
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setUsername("nil");
        userDTO1.setPassword("admin123");
        userDTO1.setFirstName("Raman");
        userDTO1.setLastName("Mehta");
        userDTO1.setRole("Admin");
        userDTOList.add(userDTO);
        userDTOList.add(userDTO1);
        given(userService.getAllUser()).willReturn(userDTOList);

        /* when */
        ResultActions response = mockMvc.perform(get("/v1/users"));

        /* then */
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(userDTOList.size())));

    }

    @Test
    public void testGetAllUser_whenGetAllUsers_thenReturnNoContent()
            throws Exception {

        /* given */
        List<UserDTO> userDTOList = new ArrayList<>();
        given(userService.getAllUser()).willReturn(Collections.emptyList());

        /* when */
        ResultActions response = mockMvc.perform(get("/v1/users"));

        /* then */
        response.andExpect(status().isNoContent());
    }

    @Test
    public void testGetUser_whenGetUserById_thenReturnUserDTO() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        given(userService.getUser(userId)).willReturn(userDTO);

        /* when */
        ResultActions response = mockMvc.perform(get("/v1/users/{userId}", userId));

        /* then */
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username",
                        CoreMatchers.is(userDTO.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",
                        CoreMatchers.is(userDTO.getPassword())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(userDTO.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(userDTO.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role",
                        CoreMatchers.is(userDTO.getRole())));
    }

    @Test
    public void testGetUser_givenInvalidUserId_whenGetUserById_thenReturnEmpty() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        given(userService.getUser(userId)).willReturn(null);

        /* when */
        ResultActions response = mockMvc.perform(get("/v1/users/{userId}", userId));

        /* then */
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateUser_whenUpdateUser_thenReturnSavedUser() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        given(userService.updateUser(ArgumentMatchers.any(UserDTO.class)))
                .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andExpect(MockMvcResultMatchers
                        .status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username",
                        CoreMatchers.is(userDTO.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",
                        CoreMatchers.is(userDTO.getPassword())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(userDTO.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(userDTO.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role",
                        CoreMatchers.is(userDTO.getRole())));
    }

    @Test
    public void testUpdateUser_givenInvalidArgs_whenUpdateUser_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setUsername("");
        given(userService.updateUser(userDTO)).willThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andExpect(MockMvcResultMatchers
                .status().isBadRequest()).andDo(print());
    }

    @Test
    public void testUpdateUser_givenInvalidId_whenUpdateUser_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(UUID.randomUUID());

        given(userService.updateUser(userDTO)).willThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andExpect(MockMvcResultMatchers
                .status().isBadRequest()).andDo(print());
    }

    @Test
    public void testDeleteUser_whenDeleteUser_thenReturn200() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        willDoNothing().given(userService).deleteUser(userId);

        /* when */
        ResultActions response = mockMvc.perform(delete("/v1/users/{userId}",
                userId));

        /* then */
        response.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteUser_givenInvalidId_whenDeleteUser_thenReturn200() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        //given(userService)..willThrow(ResourceNotFoundException.class);
        willThrow(ResourceNotFoundException.class).given(userService).deleteUser(userId);

        /* when */
        ResultActions response = mockMvc.perform(delete("/v1/users/{userId}",
                userId));

        /* then */
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}
