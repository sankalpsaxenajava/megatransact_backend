package com.megatransact.controller;

import com.megatransact.dto.UserDto;
import com.megatransact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        String result = userService.registerUser(userDto);
        return ResponseEntity.ok(result);
    }
}

