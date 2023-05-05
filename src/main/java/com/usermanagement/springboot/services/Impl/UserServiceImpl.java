package com.usermanagement.springboot.services.Impl;

import com.usermanagement.springboot.daos.UserDao;
import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.entities.User;
import com.usermanagement.springboot.exceptions.ResourceNotFoundException;
import com.usermanagement.springboot.exceptions.UserNameAlreadyExistException;
import com.usermanagement.springboot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User user = userDTO.convert(userDTO);
        if (userDao.existsByUserName(user.getUserName())) {
            throw new UserNameAlreadyExistException("User Name Already Exists");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return user.convert(userDao.save(user));
    }

    @Override
    public List<UserDTO> getAllUser() {

        List<User> userList = userDao.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        userList.forEach(user -> userDTOList.add(user.convert(user)));
        return userDTOList;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {

        User user = userDTO.convert(userDTO);
        Optional<User> userDb = userDao.findUserByUserName(user.getUserName());
        Optional<User> getUser = Optional.ofNullable(userDao.findById(user.getUserId()).
                orElseThrow(
                        () -> new ResourceNotFoundException("User", "id", String.valueOf(user.getUserId()))
                )
        );
        if (userDb.isPresent() && user.getUserId() != userDb.get().getUserId()) {
            throw new UserNameAlreadyExistException("User Name Not Available");
        }
        User updateUser = getUser.get();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        updateUser.setUserName(user.getUserName());
        updateUser.setPassword(encryptedPassword);
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setRole(user.getRole());
        User user1 = userDao.save(updateUser);
        return user1.convert(user1);
    }

    @Override
    public UserDTO getUser(UUID userId) {

        Optional<User> user = Optional.ofNullable(userDao.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User", "userId", String.valueOf(userId)))
        );
        User user1 = user.get();
        return user1.convert(user1);
    }

    @Override
    public boolean deleteUser(UUID userId) {

        userDao.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", String.valueOf(userId))
        );
        userDao.deleteById(userId);
        return true;
    }
}
