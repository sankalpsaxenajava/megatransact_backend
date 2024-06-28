package com.megatransact.controller;

import com.megatransact.dto.AuthenticationRequestDTO;
import com.megatransact.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final Environment environment;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, Environment environment) {
        this.authenticationService = authenticationService;
        this.environment = environment;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequestDTO authRequest) {
        String token = authenticationService.authenticate(authRequest.getEmail(), authRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/set-pin")
    public ResponseEntity<String> setPin(@RequestParam String email, @RequestParam String pin){
        authenticationService.setPin(email, pin);
        return ResponseEntity.ok("Your pin set successfully");
    }

    @PostMapping("/forget-password")
    public String forgetPassword(@RequestParam String email) {
        String response = authenticationService.forgetPassword(email);

        if (!response.startsWith("User does not")) {
            response = environment.getProperty("env.server_url") + "/api/auth/reset-password?token=" + response;
        }
        return response;
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        authenticationService.resetPassword(token,password);
        return ResponseEntity.ok("Your password reset successfully");
    }
}
