package com.usermanagement.springboot.services;


import com.usermanagement.springboot.daos.UserDAO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserDAO userDAO;
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
    public void givenUserEntity_whenSaveUser_thenReturnUserEntity() {

        /* given */
        given(userDAO.existsByUsername(user.getUsername())).willReturn(false);
        given(userDAO.save(user)).willReturn(user);

        /* when */
        UserDTO savedUserDTO = userService.createUser(userDTO);

        /* then */
        assertNotNull(savedUserDTO);
    }

    @Test
    public void givenExistingUserName_whenSaveUser_thenThrowsException() {

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
    public void givenUsersList_whenGetAllUsers_thenReturnUserDTOSList() {

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
    public void givenEmptyUserList_whenGetAllUsers_thenReturnEmptyUserDTOList() {

        /* given */
        given(userDAO.findAll()).willReturn(Collections.emptyList());

        /* when */
        List<UserDTO> userDTOList = userService.getAllUser();

        /* then */
        assertThat(userDTOList).isEmpty();
        assertThat(userDTOList.size()).isEqualTo(0);
    }

    @Test
    public void givenUserEntity_whenUpdateUser_thenReturnUpdatedUser() {

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
    public void givenInvalidId_whenUpdateUser_thenThrowsException() {

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
    public void givenExistingUsername_whenUpdateUser_thenThrowsException() {

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
    public void givenUserId_whenGetByUserId_thenReturnUserEntity() {

        /* given */
        given(userDAO.findById(user.getUserId())).willReturn(Optional.of(user));

        /* when */
        UserDTO savedUser = userService.getUser(user.getUserId());

        /* then */
        assertThat(savedUser).isNotNull();
    }

    @Test
    public void givenInvalidUserId_whenGetByUserId_thenThrowsException() {

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
    public void givenUserId_whenDeleteUser_thenNothing() {

        /* given */
        given(userDAO.findById(user.getUserId())).willReturn(Optional.of(user));
        willDoNothing().given(userDAO).deleteById(user.getUserId());

        /* when */
        userService.deleteUser(user.getUserId());

        /* then */
        verify(userDAO, times(1)).deleteById(user.getUserId());
    }

    @Test
    public void givenInvalidUserId_whenDeleteUser_thenThrowsException() {

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
}