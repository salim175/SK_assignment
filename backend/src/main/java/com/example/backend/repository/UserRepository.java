package com.example.backend.repository;

import com.example.backend.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);

    Boolean existsByUserEmail(String userEmail);
    boolean existsByUserPhoneNumber(String userPhoneNumber);
}
