package com.megatransact.controller;

import com.megatransact.dto.UserDTO;
import com.megatransact.dto.UserUpdateDTO;
import com.megatransact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService ) {
        this.userService = userService;
    }

    //Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDto) {
        String result = userService.registerUser(userDto);
        return ResponseEntity.ok(result);
    }


    //Update user info
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        String result = userService.updateUser(userUpdateDTO);
        if (result.equals("User info updated successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

}

