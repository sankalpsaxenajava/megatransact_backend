package com.megatransact.service;

import com.megatransact.exception.CustomExceptions;
import com.megatransact.model.User;
import com.megatransact.repository.UserRepository;
import com.megatransact.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
/**
 * This class is responsible for authenticating users and generating tokens for them.
 * It also provides methods for setting a pin, generating a forget password link, and resetting the password.
 * @author romulo.domingos
 * @since 1.0
 * @version 1.0
 */


@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private static final long EXPIRE_TOKEN=60; //forget password token expires in 60 minutes

    @Autowired
    public AuthenticationService(UserRepository userRepository, @Lazy AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetails userDetails = loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    public void setPin(String email, String pin){
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()){
            throw new CustomExceptions.UserNotFound(email);
        }

        //pin should consist 0-9 only and it should be 5 digits
        if (!(pin.matches("[0-9]+") && (pin.length() ==5))) { //matches("[0-9]+]"
            throw new CustomExceptions.InvalidArgument(pin);
        }else{
            User user = userOptional.get();

            user.setPin(passwordEncoder.encode(pin));
            userRepository.save(user);
        }
    }

    //generate a forget password link
    public String forgetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()){
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
    public void resetPassword(String token, String password) {
        Optional<User> userOptional= Optional.ofNullable(userRepository.findByToken(token));

        if(userOptional.isEmpty()){
            throw new CustomExceptions.Unauthorized(token);
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            throw new CustomExceptions.Unauthorized(token);
        }

        User user = userOptional.get();

        user.setPassword(password);
        user.setToken(null);
        user.setTokenCreationDate(null);

        userRepository.save(user);

    }

}
