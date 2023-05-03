package com.usermanagement.springboot.services;
import com.usermanagement.springboot.dtos.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public UserDTO createUser(UserDTO userDTO);

    public UserDTO updateUser(UUID userId, UserDTO user);

    public UserDTO getUser(UUID userId);

    public boolean deleteUser(UUID userId);

    public List<UserDTO> getAllUser();
}
