package com.usermanagement.springboot.services.Impl;

import com.usermanagement.springboot.daos.UserDAO;
import com.usermanagement.springboot.dtos.PasswordDTO;
import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.entities.User;
import com.usermanagement.springboot.exceptions.ResourceNotFoundException;
import com.usermanagement.springboot.exceptions.UserNameAlreadyExistException;
import com.usermanagement.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.usermanagement.springboot.common.Constants.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDao;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User user = new User();
        user.convert(userDTO);
        if (userDao.existsByUsername(user.getUsername())) {
            throw new UserNameAlreadyExistException(USER_NAME_ALREADY_EXIST);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return UserDTO.builder()
                .build()
                .convert(userDao.save(user));
    }

    @Override
    public List<UserDTO> getAllUser() {

        List<User> userList = userDao.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        userList.forEach(user -> {
            userDTOList.add(
                    UserDTO.builder()
                            .build().convert(user)
            );
        });
        return userDTOList;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {

        User user = new User();
        user.convert(userDTO);
        Optional<User> userDb = userDao.findUserByUsername(user.getUsername());
        Optional<User> getUser = Optional.ofNullable(userDao.findById(user.getUserId()).
                orElseThrow(
                        () -> new ResourceNotFoundException(USER,
                                ID,
                                String.valueOf(user.getUserId())
                        )
                )
        );
        if (userDb.isPresent() && user.getUserId() != userDb.get().getUserId()) {
            throw new UserNameAlreadyExistException(USER_NAME_ALREADY_EXIST);
        }
        User updateUser = getUser.get();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        updateUser.setUsername(user.getUsername());
        updateUser.setPassword(encryptedPassword);
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setRole(user.getRole());
        return UserDTO.builder()
                .build()
                .convert(userDao.save(updateUser));
    }

    @Override
    public UserDTO getUser(UUID userId) {

        Optional<User> user = Optional
                .ofNullable(userDao.findById(userId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(USER,
                                        USERID,
                                        String.valueOf(userId))
                        )
                );
        return UserDTO.builder()
                .build()
                .convert(user.get());
    }

    @Override
    public boolean deleteUser(UUID userId) {

        userDao.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(USER, USERID, String.valueOf(userId))
        );
        userDao.deleteById(userId);
        return true;
    }

    @Override
    public void changePassword(PasswordDTO passwordDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            String username = authentication.getName();
            Optional<User> user = Optional.ofNullable(
                    userDao.findUserByUsername(username).orElseThrow(
                            () -> new ResourceNotFoundException(
                                    USERNAME,
                                    USER_NOT_EXIST,
                                    username
                            )
                    )
            );
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (!bCryptPasswordEncoder.matches(passwordDTO.getOldPassword(), user.get().getPassword())) {
                throw new BadCredentialsException(PASSWORD_NOT_MATCH);
            }
            User user1 = user.get();
            user1.setPassword(bCryptPasswordEncoder.encode(passwordDTO.getNewPassword()));
            userDao.save(user1);
        }
    }


}
