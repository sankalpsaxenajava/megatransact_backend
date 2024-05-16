package com.megatransact.controller;

import com.megatransact.dto.UserDto;
import com.megatransact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        String result = userService.registerUser(userDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/forget-password")
    public String forgetPassword(@RequestParam String email){
        String response = userService.forgetPassword(email);

        if(!response.startsWith("User does not")){
            response= environment.getProperty("env.server_url")+"/api/users/reset-password?token=" + response;
        }
        return response;
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password){
        return userService.resetPassword(token,password);
    }
}

