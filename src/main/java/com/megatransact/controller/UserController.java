package com.megatransact.controller;

import com.megatransact.dto.UserDto;
import com.megatransact.dto.UserUpdateDTO;
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
    public ResponseEntity<String> forgetPassword(@RequestParam String email) {
        String token = userService.forgetPassword(email);
        return ResponseEntity.ok(environment.getProperty("env.server_url")+"/api/users/reset-password?token=" + token);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        userService.resetPassword(token,password);
        return ResponseEntity.ok("Your password reset successfully");
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

    @PostMapping("/set-pin")
    public ResponseEntity<String> setPin(@RequestParam String email, @RequestParam String pin){
        userService.setPin(email, pin);
        return ResponseEntity.ok("Your pin set successfully");
    }
}

