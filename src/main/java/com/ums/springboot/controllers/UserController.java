package com.ums.springboot.controllers;

import com.ums.springboot.dtos.UserDTO;
import com.ums.springboot.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("v2/users")
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {

        UserDTO userDTO1 = userService.createUser(userDTO);
        return new ResponseEntity<>(userDTO1, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> userDTOList = userService.getAllUser();
        if (userDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") UUID userId) {

        UserDTO userDTO = userService.getUser(userId);
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") UUID userId, @RequestBody @Valid UserDTO userDTO) {

        userDTO.setUserId(userId);
        UserDTO userDTO1 = userService.updateUser(userDTO);
        return new ResponseEntity<>(userDTO1, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("userId") UUID userId) {

        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}