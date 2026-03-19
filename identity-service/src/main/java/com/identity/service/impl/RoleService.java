package com.identity.service.impl;

import com.identity.dto.request.RoleReq;
import com.identity.dto.response.RoleResponse;
import com.identity.entity.Permission;
import com.identity.entity.Role;
import com.identity.exception.ErrorCode;
import com.identity.exception.ServiceException;
import com.identity.mapper.RoleMapper;
import com.identity.repo.PermissionRepo;
import com.identity.repo.RoleRepo;
import com.identity.service.IRoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService {
    RoleMapper roleMapper;
    RoleRepo roleRepo;
    PermissionRepo permissionRepo;

    @Override
    public RoleResponse createRole(RoleReq roleReq) {
        List<Permission> permissions = permissionRepo.findAllById(
                roleReq.getPermissions()
        );
        Role role = roleMapper.toRoleFormRoleReq(roleReq);
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResFromRole(roleRepo.save(role));
    }

    @Override
    public void deleteRole(String role) {
        roleRepo.findById(role)
                .orElseThrow(() -> new ServiceException(ErrorCode.ROLE_3002));
        roleRepo.deleteById(role);
    }

    @Override
    public RoleResponse getRole(String role) {
        Role roleExisted = roleRepo.findById(role)
                .orElseThrow(() -> new ServiceException(ErrorCode.ROLE_3002));
        return roleMapper.toRoleResFromRole(roleExisted);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepo.findAll().stream()
                .map(roleMapper::toRoleResFromRole).toList();
    }
}
