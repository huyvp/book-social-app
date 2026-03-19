package com.identity.repo;

import com.identity.entity.Permission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepo extends JpaRepository<Permission, String> {
}
