package com.usermanagement.springboot.services.Impl;

import com.usermanagement.springboot.daos.UserDAO;
import com.usermanagement.springboot.dtos.PasswordDTO;
import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.entities.User;
import com.usermanagement.springboot.exceptions.ResourceNotFoundException;
import com.usermanagement.springboot.exceptions.UserNameAlreadyExistException;
import com.usermanagement.springboot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserDAO userDao;


    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User user = userDTO.convert(userDTO);
        if (userDao.existsByUsername(user.getUsername())) {
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
        Optional<User> userDb = userDao.findUserByUsername(user.getUsername());
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
        updateUser.setUsername(user.getUsername());
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

    @Override
    public boolean changePassword(PasswordDTO passwordDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            String username = authentication.getName();
            Optional<User> user = Optional.ofNullable(
                    userDao.findUserByUsername(username).orElseThrow(
                            () -> new ResourceNotFoundException("username", "No User Exist", username)
                    )
            );
            if (!user.isPresent()) {

                return false;
            }
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if (!bCryptPasswordEncoder.matches(passwordDTO.getOldPassword(), user.get().getPassword())) {
                System.out.println("Password is not matching");
                return false;
            }
            user.ifPresent(user1 -> {

                user1.setPassword(bCryptPasswordEncoder.encode(passwordDTO.getNewPassword()));
                userDao.save(user1);
            });

        }
        return true;
    }


}
