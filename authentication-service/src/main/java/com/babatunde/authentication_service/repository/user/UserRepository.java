package com.babatunde.authentication_service.repository.user;


import com.babatunde.authentication_service.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String email);

    Optional<User> findByUsername(String email);
}