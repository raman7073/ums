package com.usermanagement.springboot.services;

import com.usermanagement.springboot.dtos.PasswordDTO;
import com.usermanagement.springboot.dtos.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO user);

    UserDTO getUser(UUID userId);

    void deleteUser(UUID userId);

    void changePassword(PasswordDTO passwordDTO);

    List<UserDTO> getAllUser();
}
