package com.ums.springboot.services.Impl;

import com.ums.springboot.daos.UserDAO;
import com.ums.springboot.dtos.UserDTO;
import com.ums.springboot.entities.User;
import com.ums.springboot.exceptions.ResourceNotFoundException;
import com.ums.springboot.exceptions.UserNameAlreadyExistException;
import com.ums.springboot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if (userDao.existByUserName(user.getUserName())) {
            throw new UserNameAlreadyExistException("User Name Already Exists");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setCreatedAt(LocalDateTime.now());
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
        List<User> userDbList = userDao.findByUserName(user.getUserName());
        int flag = 0;
        for (User user1 : userDbList) {
            if ((!user1.getUserId().equals(user.getUserId()))
                    && (user1.getUserName().equals(user.getUserName()))) {
                flag = 1;
                break;
            }
        }
        if (flag == 1) {
            throw new UserNameAlreadyExistException("User Name Not Available");
        }
        Optional<User> getUser = Optional.ofNullable(
                userDao.findById(user.getUserId()).orElseThrow(
                        () -> new ResourceNotFoundException("User", "id",
                                String.valueOf(user.getUserId())
                        )
                )
        );
        User updateUser = getUser.get();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        updateUser.setUserId(updateUser.getUserId());
        updateUser.setUserName(user.getUserName());
        updateUser.setPassword(encryptedPassword);
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setRole(user.getRole());
        updateUser.setCreatedAt(updateUser.getCreatedAt());
        updateUser.setUpdatedAt(LocalDateTime.now());
        updateUser.setDeleted(false);
        User user1 = userDao.update(user.getUserId(), updateUser);
        return user1.convert(user1);
    }


    @Override
    public UserDTO getUser(UUID uuid) {
        Optional<User> user = Optional.ofNullable(
                userDao.findById(uuid).orElseThrow(
                        () -> new ResourceNotFoundException("User", "uuid",
                                String.valueOf(uuid))
                )
        );
        User user1 = user.get();
        if (user1.isDeleted()) return null;
        return user1.convert(user1);
    }

    @Override
    public boolean deleteUser(UUID uuid) {

        userDao.findById(uuid).orElseThrow(
                () -> new ResourceNotFoundException("User", "uuid", String.valueOf(uuid))
        );
        userDao.deleteById(uuid);
        return true;
    }
}
