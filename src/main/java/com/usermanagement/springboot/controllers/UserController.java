package com.usermanagement.springboot.controllers;


import com.usermanagement.springboot.dtos.UserDTO;
import com.usermanagement.springboot.entities.User;
import com.usermanagement.springboot.mappers.UserDtoToUserEntity;
import com.usermanagement.springboot.mappers.UserEntityToUserDTO;
import com.usermanagement.springboot.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("ums/v1/users")
public class UserController {

    private UserService userService;
    private UserEntityToUserDTO userEntityToUserDTO;
    private UserDtoToUserEntity userDtoToUserEntity;

    //build Create User Rest Api
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
     /*
       Own method for validation
        UserDtoValidator.isValid(userDTO);
      */
   /*
        User userEntity = UserMapper.convertUserDtoToEntity(userDTO);
        other ways are use libraries like ModelMapper or MapStruct
   */
        User userEntity = userDtoToUserEntity.convert(userDTO);
        User savedUserEntity = userService.createUser(userEntity);
        UserDTO userDTO1 = userEntityToUserDTO.convert(savedUserEntity);
        return new ResponseEntity<>(userDTO1, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<User> userList = userService.getAllUser();

        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : userList) {
            UserDTO userDTO = userEntityToUserDTO.convert(user);
            userDTOS.add(userDTO);
        }
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);

    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("uuid") UUID uuid) {

        User user = userService.getUser(uuid);
        UserDTO userDTO = userEntityToUserDTO.convert(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);


    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("uuid") UUID uuid, @RequestBody @Valid UserDTO userDTO) {

       /*
          UserDtoValidator.isValid(userDTO);
        */
        User userEntity = userDtoToUserEntity.convert(userDTO);
        User savedUser = userService.updateUser(uuid, userEntity);
        UserDTO userDTO1 = userEntityToUserDTO.convert(savedUser);
        return new ResponseEntity<>(userDTO1, HttpStatus.OK);

    }

    @DeleteMapping("/{uuid}")
    public String deleteUser(@PathVariable("uuid") UUID uuid) {

        userService.deleteUser(uuid);
        return "Deleted Successfully";
        //new ResponseEntity<>(HttpStatus.OK);

    }

//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest){
//        ErrorDetails errorDetails = new ErrorDetails(
//                LocalDateTime.now(),
//                exception.getMessage(),
//                webRequest.getDescription(false),
//                "User Not Found"
//        );
//        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
//    }
}
