package com.usermanagement.springboot.services;

import com.usermanagement.springboot.dtos.PasswordDTO;
import com.usermanagement.springboot.dtos.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public UserDTO createUser(UserDTO userDTO);

    public UserDTO updateUser(UserDTO user);

    public UserDTO getUser(UUID userId);

    public boolean deleteUser(UUID userId);

    public void changePassword(PasswordDTO passwordDTO);

    public List<UserDTO> getAllUser();
}
