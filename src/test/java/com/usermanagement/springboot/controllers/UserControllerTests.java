package com.usermanagement.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usermanagement.springboot.daos.UserDAO;
import com.usermanagement.springboot.dtos.UserDTO;
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
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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
    public void givenUserDTO_whenCreateUser_thenReturnSavedUser() throws Exception {

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
        response.andExpect(MockMvcResultMatchers.status().isCreated())
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
    public void givenListOfUserDTOs_whenGetAllUsers_thenReturnUserDTOList()
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
    public void givenUserId_whenGetUserById_thenReturnUserDTO() throws Exception {

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
    public void givenInvalidUserId_whenGetUserById_thenReturnEmpty() throws Exception {

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
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

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
}
