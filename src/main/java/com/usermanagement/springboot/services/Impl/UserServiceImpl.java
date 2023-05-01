package com.usermanagement.springboot.services.Impl;

import com.usermanagement.springboot.daos.UserDao;
import com.usermanagement.springboot.entities.User;
import com.usermanagement.springboot.exceptions.NoContentExistException;
import com.usermanagement.springboot.exceptions.ResourceNotFoundException;
import com.usermanagement.springboot.exceptions.UserNameAlreadyExistException;
import com.usermanagement.springboot.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;


    @SneakyThrows //as it throws exception
    @Override
    public User createUser(User user) {

        // for concurrent environment we need to check database
        if (userDao.existsByuserName(user.getUserName())) {
            throw new UserNameAlreadyExistException("User Name Already Exists");
        }


        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return userDao.save(user);

    }

    @SneakyThrows
    @Override
    public List<User> getAllUser() {
        List<User> userList = userDao.findAll();
        if (userList.isEmpty()) {
            throw new NoContentExistException("No Content Exists");
        }
        return userList;
    }

    @Override
    public User updateUser(UUID uuid, User user) {


        Optional<User> getUser = Optional.ofNullable(userDao.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", String.valueOf(uuid)))
        );

        Optional<User> userDb = userDao.findUserByuserName(user.getUserName());

        if (userDb.isPresent() && uuid != userDb.get().getUuid()) {
            throw new UserNameAlreadyExistException("User Name Not Available");
        }
        User updateUser = getUser.get();
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setRole(user.getRole());
        return userDao.save(updateUser);

    }


    @Override
    public User getUser(UUID uuid) {
        Optional<User> user = Optional.ofNullable(userDao.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User", "uuid", String.valueOf(uuid))));
        return user.get();
    }

    @Override
    public boolean deleteUser(UUID uuid) {

        User existingUser = userDao.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User", "uuid", String.valueOf(uuid)));
        userDao.deleteById(uuid);
        return true;

    }


}
