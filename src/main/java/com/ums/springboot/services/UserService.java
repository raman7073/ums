package com.ums.springboot.services;

import com.ums.springboot.dtos.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public UserDTO createUser(UserDTO userDTO);

    public UserDTO updateUser(UserDTO userDTO);

    public UserDTO getUser(UUID userId);

    public boolean deleteUser(UUID userId);

    public List<UserDTO> getAllUser();
}
