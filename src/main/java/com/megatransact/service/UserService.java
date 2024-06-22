package com.megatransact.service;

import com.megatransact.dto.UserDTO;
import com.megatransact.dto.UserUpdateDTO;
import com.megatransact.model.User;
import com.megatransact.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Register a new user and validate if user already exists.
     * @param @UserDto userDto
     * @return String
     */
    public String registerUser(UserDTO userDto) {
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


    public String updateUser(UserUpdateDTO userUpdateDTO) {
        Optional<User> userOptional = userRepository.findByEmail(userUpdateDTO.getEmail());

        if (userOptional.isEmpty()) {
            return "User does not exist";
        }

        User user = userOptional.get();
        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());

        userRepository.save(user);
        return "User info updated successfully";
    }
}
