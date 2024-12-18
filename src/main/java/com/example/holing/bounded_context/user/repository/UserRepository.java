package com.example.holing.bounded_context.user.repository;

import com.example.holing.bounded_context.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findBySocialId(Long socialId);
}
