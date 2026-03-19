package com.identity.repo;

import com.identity.entity.InvalidatedToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepo extends JpaRepository<InvalidatedToken, String> {
}
