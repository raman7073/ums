package com.usermanagement.springboot.services;


import com.usermanagement.springboot.daos.UserDAO;
import com.usermanagement.springboot.dtos.PasswordDTO;
import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.entities.User;
import com.usermanagement.springboot.exceptions.ResourceNotFoundException;
import com.usermanagement.springboot.exceptions.UserNameAlreadyExistException;
import com.usermanagement.springboot.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserDAO userDAO;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void setup() {

        user = new User();
        UUID userId = UUID.randomUUID();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setUsername("geeky");
        user.setUserId(userId);
        user.setPassword(bCryptPasswordEncoder.encode("admin123"));
        user.setFirstName("Raman");
        user.setLastName("Mehta");
        user.setRole("Admin");
        userDTO = new UserDTO();
        userDTO.convert(user);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testCreateUserEntity_whenSaveUser_thenReturnUserEntity() {

        /* given */
        given(userDAO.existsByUsername(user.getUsername())).willReturn(false);
        given(userDAO.save(user)).willReturn(user);

        /* when */
        UserDTO savedUserDTO = userService.createUser(userDTO);

        /* then */
        assertNotNull(savedUserDTO);
    }

    @Test
    public void testCreateUserEntity_givenExistingUserName_whenSaveUser_thenThrowsException() {

        /* given */
        given(userDAO.existsByUsername(user.getUsername())).willReturn(true);

        /* when */
        assertThrows(UserNameAlreadyExistException.class,
                () -> userService.createUser(userDTO)
        );

        /* then */
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    public void testGetAllUser_givenUsersList_whenGetAllUsers_thenReturnUserDTOSList() {

        /* given  */
        User user1 = new User();
        given(userDAO.findAll()).willReturn(List.of(user, user1));

        /* when */
        List<UserDTO> userDTOList = userService.getAllUser();

        /* then */
        assertThat(userDTOList).isNotNull();
        assertThat(userDTOList.size()).isEqualTo(2);
    }

    @Test
    public void testGetAllUser_givenEmptyUserList_whenGetAllUsers_thenReturnEmptyUserDTOList() {

        /* given */
        given(userDAO.findAll()).willReturn(Collections.emptyList());

        /* when */
        List<UserDTO> userDTOList = userService.getAllUser();

        /* then */
        assertThat(userDTOList).isEmpty();
        assertThat(userDTOList.size()).isEqualTo(0);
    }

    @Test
    public void testUpdateUser_givenUserEntity_whenUpdateUser_thenReturnUpdatedUser() {

        /* given  */
        userDTO.setUsername("advt");
        userDTO.setFirstName("Ram");
        user.setUsername("advt");
        user.setFirstName("Ram");
        given(userDAO.findUserByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(userDAO.findById(userDTO.getUserId())).willReturn(Optional.of(user));
        given(userDAO.save(user)).willReturn(user);

        /* when */
        UserDTO updatedUser = userService.updateUser(userDTO);

        /* then */
        assertThat(updatedUser.getUsername()).isEqualTo("advt");
        assertThat(updatedUser.getFirstName()).isEqualTo("Ram");
    }

    @Test
    public void testUpdateUser_givenInvalidId_whenUpdateUser_thenThrowsException() {

        /* given */
        UUID userId = UUID.randomUUID();
        userDTO.setUserId(userId);
        given(userDAO.findById(userId)).willReturn(Optional.empty());

        /* when */
        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(userDTO)
        );

        /* then */
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_givenExistingUsername_whenUpdateUser_thenThrowsException() {

        /* given */
        UUID userId1 = UUID.randomUUID();
        User user1 = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user1.setUserId(userId1);
        user1.setUsername("geeky");
        user1.setPassword(bCryptPasswordEncoder.encode("admin123"));
        user1.setFirstName("Aman");
        user1.setLastName("Mehta");
        user1.setRole("Admin");
        UserDTO userDTO1 = new UserDTO();
        userDTO1.convert(user1);
        given(userDAO.findUserByUsername(userDTO.getUsername()))
                .willReturn(Optional.of(user));
        given(userDAO.findById(userDTO1.getUserId())).willReturn(Optional.of(user1));

        /* when */
        assertThrows(UserNameAlreadyExistException.class,
                () -> userService.updateUser(userDTO1)
        );

        /* then */
        verify(userDAO, never()).save(any(User.class));
    }

    @Test
    public void testGetUser_givenUserId_whenGetByUserId_thenReturnUserEntity() {

        /* given */
        given(userDAO.findById(user.getUserId())).willReturn(Optional.of(user));

        /* when */
        UserDTO savedUser = userService.getUser(user.getUserId());

        /* then */
        assertThat(savedUser).isNotNull();
    }

    @Test
    public void testGetUser_givenInvalidUserId_whenGetByUserId_thenThrowsException() {

        /* given */
        UUID userId = UUID.randomUUID();
        given(userDAO.findById(userId)).willReturn(Optional.empty());

        /* when */
        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUser(userId)
        );

        /* then */
        verify(userDAO, times(1)).findById(userId);
    }

    @Test
    public void testDeleteUser_givenUserId_whenDeleteUser_thenNothing() {

        /* given */
        given(userDAO.findById(user.getUserId())).willReturn(Optional.of(user));
        willDoNothing().given(userDAO).deleteById(user.getUserId());

        /* when */
        userService.deleteUser(user.getUserId());

        /* then */
        verify(userDAO, times(1)).deleteById(user.getUserId());
    }

    @Test
    public void testDeleteUser_givenInvalidUserId_whenDeleteUser_thenThrowsException() {

        /* given */
        UUID userId = UUID.randomUUID();
        given(userDAO.findById(userId)).willReturn(Optional.empty());

        /* when */
        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        /*then*/
        verify(userDAO, times(0)).deleteById(userId);
    }

    @Test
    public void testChangePassword_givenPasswordDTO_whenChangePassword_thenSaveUserWithNewPassword() {

        /* given */
        String username = "tester";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        given(authentication.getName()).willReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setPassword(passwordEncoder.encode(oldPassword));
        given(userDAO.findUserByUsername(username)).willReturn(Optional.of(existingUser));

        /* when */
        PasswordDTO passwordDTO = new PasswordDTO(oldPassword, newPassword);
        userService.changePassword(passwordDTO);

        /* then */
        verify(userDAO, times(1)).findUserByUsername(username);
        verify(userDAO, times(1)).save(existingUser);
        assertTrue(passwordEncoder.matches(newPassword, existingUser.getPassword()));
    }

    @Test
    public void testChangePassword_givenInvalidOldPassword_whenChangePassword_thenThrowException() {

        /* given */
        String username = "tester";
        String oldPassword = "wrongPassword";
        String newPassword = "newPassword";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("admin123");
        given(authentication.getName()).willReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setPassword(encodedPassword);
        given(userDAO.findUserByUsername(username)).willReturn(Optional.of(existingUser));

        /* when */
        PasswordDTO passwordDTO = new PasswordDTO(oldPassword, newPassword);
        assertThrows(BadCredentialsException.class, () -> userService.changePassword(passwordDTO));

        /* then */
        verify(userDAO, times(1)).findUserByUsername(username);
        verify(userDAO, never()).save(existingUser);
    }
}