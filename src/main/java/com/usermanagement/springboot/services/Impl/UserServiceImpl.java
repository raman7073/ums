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
    private UserDAO userDAO;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User user = new User();
        user.convert(userDTO);
        if (user.getUsername() != null && userDAO.existsByUsername(user.getUsername())) {
            throw new UserNameAlreadyExistException(USER_NAME_ALREADY_EXIST);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (user.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userDAO.save(user);
        return userDTO.convert(user);
    }

    @Override
    public List<UserDTO> getAllUser() {

        List<User> userList = userDAO.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        UserDTO userDTO = new UserDTO();
        userList.forEach(user -> userDTOList.add(
                userDTO.convert(user)
        ));
        return userDTOList;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {

        Optional<User> getUserById = Optional.ofNullable(userDAO
                .findById(userDTO.getUserId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(USER,
                                ID,
                                String.valueOf(userDTO.getUserId())
                        )
                )
        );
        User user = getUserById.get();
        Optional<User> getUserByUsername = userDAO
                .findUserByUsername(userDTO.getUsername());
        if (getUserByUsername.isPresent() &&
                user.getUserId() != getUserByUsername.get().getUserId()) {

            throw new UserNameAlreadyExistException(USER_NAME_ALREADY_EXIST);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setUsername(userDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setRole(userDTO.getRole());
        return userDTO.convert(userDAO.save(user));
    }

    @Override
    public UserDTO getUser(UUID userId) {

        Optional<User> user = Optional
                .ofNullable(userDAO.findById(userId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(USER,
                                        USERID,
                                        String.valueOf(userId))
                        )
                );
        UserDTO userDTO = new UserDTO();
        return userDTO.convert(user.get());
    }

    @Override
    public void deleteUser(UUID userId) {

        userDAO.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException(USER, USERID, String.valueOf(userId))
        );
        userDAO.deleteById(userId);
    }

    @Override
    public void changePassword(PasswordDTO passwordDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            String username = authentication.getName();
            Optional<User> user = Optional.ofNullable(
                    userDAO.findUserByUsername(username).orElseThrow(
                            () -> new ResourceNotFoundException(
                                    USERNAME,
                                    USER_NOT_EXIST,
                                    username
                            )
                    )
            );
            user.ifPresent(user1 -> {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (!bCryptPasswordEncoder.matches(passwordDTO.getCurrentPassword(),
                        user1.getPassword())) {

                    throw new BadCredentialsException(PASSWORD_NOT_MATCH);
                }
                user1.setPassword(bCryptPasswordEncoder.encode(passwordDTO.getNewPassword()));
                userDAO.save(user1);

            });
        }
    }
}
