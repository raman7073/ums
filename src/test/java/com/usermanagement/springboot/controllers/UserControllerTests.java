package com.usermanagement.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usermanagement.springboot.daos.UserDAO;
import com.usermanagement.springboot.dtos.PasswordDTO;
import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.exceptions.ResourceNotFoundException;
import com.usermanagement.springboot.exceptions.UserNameAlreadyExistException;
import com.usermanagement.springboot.security.JwtTokenFilter;
import com.usermanagement.springboot.services.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.usermanagement.springboot.common.Constants.USER_NAME_ALREADY_EXIST;
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
        userDTO.setUserId(UUID.randomUUID());
        userDTO.setUsername("nial");
        userDTO.setPassword("admin123");
        userDTO.setFirstName("Raman");
        userDTO.setLastName("Mehta");
        userDTO.setRole("Admin");
    }

    @Test
    public void testCreateUser_whenCreateUser_thenReturnSavedUser() throws Exception {
        /* given */
        when(userService.createUser(any(UserDTO.class)))
                .thenAnswer(
                        invocationOnMock -> invocationOnMock.getArgument(0)
                );

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username",
                        is(userDTO.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",
                        is(userDTO.getPassword())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        is(userDTO.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        is(userDTO.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role",
                        is(userDTO.getRole())));
    }

    @Test
    public void testCreateUser_givenExistingUserName_whenCreateUser_thenReturn409() throws Exception {

        /* given */
        when(userService.createUser(ArgumentMatchers.any(UserDTO.class)))
                .thenThrow(new UserNameAlreadyExistException(USER_NAME_ALREADY_EXIST));

        /* then */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
        verify(userService, times(1))
                .createUser(ArgumentMatchers.any(UserDTO.class));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testCreateUser_givenInvalidUsername_whenCreateUser_thenThrowsException(String username) throws Exception {

        /* given */
        userDTO.setUsername(username);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testCreateUser_givenUsernameAsNull_whenCreateUser_thenThrowsException() throws Exception {

        /* given */
        userDTO.setUsername(null);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testCreateUser_givenInvalidPassword_whenCreateUser_thenThrowsException(String password) throws Exception {

        /* given */
        userDTO.setPassword(password);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testCreateUser_givenPasswordAsNull_whenCreateUser_thenThrowsException() throws Exception {

        /* given */
        userDTO.setPassword(null);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testCreateUser_givenInvalidFirstName_whenCreateUser_thenThrowsException(String firstName) throws Exception {

        /* given */
        userDTO.setFirstName(firstName);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testCreateUser_givenFirstNameAsNull_whenCreateUser_thenThrowsException() throws Exception {

        /* given */
        userDTO.setFirstName(null);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testCreateUser_givenInvalidLastName_whenCreateUser_thenThrowsException(String lastName) throws Exception {

        /* given */
        userDTO.setLastName(lastName);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testCreateUser_givenLastNameAsNull_whenCreateUser_thenThrowsException() throws Exception {

        /* given */
        userDTO.setLastName(null);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testCreateUser_givenInvalidRole_whenCreateUser_thenThrowsException(String role) throws Exception {

        /* given */
        userDTO.setRole(role);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testCreateUser_givenRoleAsNull_whenCreateUser_thenThrowsException() throws Exception {

        /* given */
        userDTO.setRole(null);
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }


    @Test
    public void testCreateUser_givenInvalidArgs_whenCreateUser_thenThrowsException() throws Exception {

        /* given */
        userDTO.setUsername("");
        userDTO.setPassword(null);
        userDTO.setRole("          ");
        when(userService.createUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(userDTO))
        );
        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
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
        when(userService.getAllUser()).thenReturn(userDTOList);

        /* when */
        ResultActions response = mockMvc.perform(
                get("/v1/users")
                        .characterEncoding("utf-8")
        );

        /* then */
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",
                        is(userDTOList.size())));

    }

    @Test
    public void testGetAllUser_whenGetAllUsers_thenReturnNoContent()
            throws Exception {

        /* given */
        List<UserDTO> userDTOList = new ArrayList<>();
        when(userService.getAllUser()).thenReturn(Collections.emptyList());

        /* when */
        ResultActions response = mockMvc.perform(
                get("/v1/users")
                        .characterEncoding("utf-8")
        );

        /* then */
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetUser_whenGetUserById_thenReturnUserDTO() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        when(userService.getUser(userId)).thenReturn(userDTO);

        /* when */
        ResultActions response = mockMvc.perform(
                get("/v1/users/{userId}", userId)
                        .characterEncoding("utf-8")
        );

        /* then */
        response.andDo(print())
                .andExpect(status().isOk())
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
    public void testGetUser_givenInvalidUserId_whenGetUserById_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        when(userService.getUser(userId)).thenThrow(ResourceNotFoundException.class);

        /* when */
        ResultActions response = mockMvc.perform(
                get("/v1/users/{userId}", userId)
                        .characterEncoding("utf-8")
        );

        /* then */
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUser_whenUpdateUser_thenReturnSavedUser() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setUsername("advt");
        when(userService.updateUser(ArgumentMatchers.any(UserDTO.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
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
        userDTO.setLastName(null);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testUpdateUser_givenInvalidUsername_whenUpdateUser_thenReturn404(String username) throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setUsername(username);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testUpdateUser_givenUsernameAsNull_whenUpdateUser_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setUsername(null);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testUpdateUser_givenInvalidPassword_whenUpdateUser_thenReturn404(String password) throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setPassword(password);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testUpdateUser_givenPasswordAsNull_whenUpdateUser_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setPassword(null);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testUpdateUser_givenInvalidFirstName_whenUpdateUser_thenReturn404(String firstName) throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setFirstName(firstName);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testUpdateUser_givenFirstNameAsNull_whenUpdateUser_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setFirstName(null);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testUpdateUser_givenInvalidLastName_whenUpdateUser_thenReturn404(String lastName) throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setLastName(lastName);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testUpdateUser_givenLastNameAsNull_whenUpdateUser_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setLastName(null);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testUpdateUser_givenInvalidRoleAs_whenUpdateUser_thenReturn404(String role) throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setRole(role);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }

    @Test
    public void testUpdateUser_givenRoleAsNull_whenUpdateUser_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        userDTO.setRole(null);
        when(userService.updateUser(userDTO)).thenThrow(NullPointerException.class);

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest());
    }


    @Test
    public void testUpdateUser_givenInvalidId_whenUpdateUser_thenReturn404() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        when(userService.updateUser(any(UserDTO.class)))
                .thenThrow(new ResourceNotFoundException("User", "ID", userId.toString()));

        /* when */
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(userDTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers
                        .status().isNotFound());
    }

    @Test
    public void testDeleteUser_whenDeleteUser_thenReturn200() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).deleteUser(userId);

        /* when */
        ResultActions response = mockMvc.perform(
                delete("/v1/users/{userId}", userId)
                        .characterEncoding("utf-8")
        );

        /* then */
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser_givenInvalidId_whenDeleteUser_thenReturn200() throws Exception {

        /* given */
        UUID userId = UUID.randomUUID();
        doThrow(ResourceNotFoundException.class).when(userService).deleteUser(userId);

        /* when */
        ResultActions response = mockMvc.perform(
                delete("/v1/users/{userId}", userId)
                        .characterEncoding("utf-8")
        );

        /* then */
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testChangepassword_whenChangePassword_thenReturnNothing() throws Exception {

        /* given */
        PasswordDTO passwordDTODTO = new PasswordDTO("oldPassword", "newPassword");
        doNothing().when(userService).changePassword(any(PasswordDTO.class));

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDTODTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testChangepassword_givenInvalidOldPassword_whenChangePassword_thenThrowsException()
            throws Exception {

        /* given */
        PasswordDTO passwordDTODTO = new PasswordDTO("oldPassword",
                "newPassword");
        doThrow(BadCredentialsException.class).when(userService)
                .changePassword(any(PasswordDTO.class));

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDTODTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testChangepassword_givenInvalidCurrentPassword_whenChangePassword_thenThrowsException(String currentPassword)
            throws Exception {

        /* given */
        PasswordDTO passwordDTODTO = new PasswordDTO(currentPassword, "newPassword");
        doThrow(NullPointerException.class)
                .when(userService)
                .changePassword(any(PasswordDTO.class));

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDTODTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testChangepassword_givenCurrentPasswordAsNull_whenChangePassword_thenThrowsException()
            throws Exception {

        /* given */
        PasswordDTO passwordDTODTO = new PasswordDTO(null, "newPassword");
        doThrow(NullPointerException.class)
                .when(userService)
                .changePassword(any(PasswordDTO.class));

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDTODTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void testChangepassword_givenInvalidNewPassword_whenChangePassword_thenThrowsException(String newPassword)
            throws Exception {

        /* given */
        PasswordDTO passwordDTODTO = new PasswordDTO("password", newPassword);
        doThrow(NullPointerException.class)
                .when(userService)
                .changePassword(any(PasswordDTO.class));

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDTODTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testChangepassword_givenNewPasswordAsNull_whenChangePassword_thenThrowsException()
            throws Exception {

        /* given */
        PasswordDTO passwordDTODTO = new PasswordDTO("password", null);
        doThrow(NullPointerException.class)
                .when(userService)
                .changePassword(any(PasswordDTO.class));

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDTODTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void testChangepassword_givenInvalidArgs_whenChangePassword_thenThrowsException()
            throws Exception {

        /* given */
        PasswordDTO passwordDTODTO = new PasswordDTO("", null);
        doThrow(NullPointerException.class)
                .when(userService)
                .changePassword(any(PasswordDTO.class));

        /* when */
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/users/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(objectMapper.writeValueAsString(passwordDTODTO)));

        /* then */
        response.andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
