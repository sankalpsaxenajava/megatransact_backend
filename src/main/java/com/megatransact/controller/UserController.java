package com.megatransact.controller;

import com.megatransact.dto.UserDTO;
import com.megatransact.dto.UserUpdateDTO;
import com.megatransact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public ResponseEntity<String> updateUser(
        @RequestBody UserUpdateDTO userUpdateDTO,
        @RequestHeader(name = "Authorization", required = false) String authentication,
        UsernamePasswordAuthenticationToken token) {
        // TODO: Example on how to obtain the header/token
        // not recommended to work with this UsernamePasswordAuthenticationToken as is right now, since it's exposing
        // user password
        // Maybe change token object model class or just extract Authorization http header on request and then decode
        String result = userService.updateUser(userUpdateDTO);
        if (result.equals("User info updated successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

}

