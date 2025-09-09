package com.DTechLtd.propertypulse_backend.repository;

import com.DTechLtd.propertypulse_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}