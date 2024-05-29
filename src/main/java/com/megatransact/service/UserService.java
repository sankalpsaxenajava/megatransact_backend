package com.megatransact.service;

import com.megatransact.dto.UserDto;
import com.megatransact.dto.UserUpdateDTO;
import com.megatransact.exception.CustomExceptions;
import com.megatransact.model.User;
import com.megatransact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private static final long EXPIRE_TOKEN=60; //forget password token expires in 60 minutes

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

    //generate a forget password link
    public String forgetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(!userOptional.isPresent()){
            throw new CustomExceptions.UserNotFound(email);
        }

        User user=userOptional.get();
        user.setToken(generateToken());
        user.setTokenCreationDate(LocalDateTime.now());

        user=userRepository.save(user);
        return user.getToken();
    }

    private String generateToken() {

        return String.valueOf(UUID.randomUUID()) +
                UUID.randomUUID();
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >=EXPIRE_TOKEN;
    }

    //reset password by forget password link
    public String resetPassword(String token, String password) {
        Optional<User> userOptional= Optional.ofNullable(userRepository.findByToken(token));

        if(!userOptional.isPresent()){
            throw new CustomExceptions.UnAuthorized(token);
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            throw new CustomExceptions.UnAuthorized(token);
        }

        User user = userOptional.get();

        user.setPassword(password);
        user.setToken(null);
        user.setTokenCreationDate(null);

        userRepository.save(user);

        return "success";
    }

    public String setPin(String email, String pin){
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(!userOptional.isPresent()){
            throw new CustomExceptions.UserNotFound(email);
        }

        //pin should consist 0-9 only and it should be 5 digits
        if (!(pin.matches("[0-9]+") && (pin.length() ==5))) { //matches("[0-9]+]"
            throw new CustomExceptions.InvalidArgument(pin);
        }else{
            User user = userOptional.get();

            user.setPin(passwordEncoder.encode(pin));
            userRepository.save(user);
            return "Your pin set successfully";
        }
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
