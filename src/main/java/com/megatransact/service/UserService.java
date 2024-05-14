package com.megatransact.service;

import com.megatransact.dto.UserDto;
import com.megatransact.model.User;
import com.megatransact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new user and validate if user already exists.
     * @param @UserDto userDto
     * @return String
     */
    public String registerUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return "user already exists";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()));
        user.setPhoneNumber(userDto.getPhoneNumber());
        
        userRepository.save(user);
        return "user created successfully";
    }
}
