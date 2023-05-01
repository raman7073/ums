package com.usermanagement.springboot.services;

import com.usermanagement.springboot.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public User createUser(User user);

    public User updateUser(UUID uuid, User user);

    public User getUser(UUID uuid);

    public boolean deleteUser(UUID uuid);

    public List<User> getAllUser();
}
