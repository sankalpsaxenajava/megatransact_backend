package com.megatransact.repository;

import com.megatransact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *  User repository interface
 * @author romulo.domingos
 * @version 1.0
 */
@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findByToken(String token);
}
