package com.usermanagement.springboot.controllers;


import com.usermanagement.springboot.dtos.PasswordDTO;
import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.usermanagement.springboot.common.Constants.*;

@RestController
@RequestMapping(V1_USERS)
public class UserController {
    @Autowired
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

    @GetMapping(VAR_USERID)
    public ResponseEntity<UserDTO> getUser(@PathVariable(USERID) UUID userId) {

        UserDTO userDTO = userService.getUser(userId);
        if(userDTO == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping(VAR_USERID)
    public ResponseEntity<UserDTO> updateUser(@PathVariable(USERID) UUID userId,
                                              @RequestBody @Valid UserDTO userDTO) {

        userDTO.setUserId(userId);
        UserDTO userDTO1 = userService.updateUser(userDTO);
        return new ResponseEntity<>(userDTO1, HttpStatus.OK);
    }

    @DeleteMapping(VAR_USERID)
    public ResponseEntity<String> deleteUser(@PathVariable(USERID) UUID userId) {

        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(DELETED);
    }

    @PostMapping(CHANGEPASSWORD)
    public ResponseEntity<String> changePassword(@RequestBody @Valid PasswordDTO passwordDTO) {

         userService.changePassword(passwordDTO);
         return ResponseEntity.status(HttpStatus.OK).body(UPDATED);
    }
}
