package com.identity.repo;

import com.identity.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByUsernameAndActiveTrue(String username);

    Optional<User> findByIdAndActiveTrue(String userId);

    Page<User> findAllByActiveTrue(Pageable pageable);

}
