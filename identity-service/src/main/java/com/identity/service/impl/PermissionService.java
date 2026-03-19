package com.identity.service.impl;

import com.identity.dto.request.PermissionReq;
import com.identity.dto.response.PermissionResponse;
import com.identity.entity.Permission;
import com.identity.exception.ErrorCode;
import com.identity.exception.ServiceException;
import com.identity.mapper.PermissionMapper;
import com.identity.repo.PermissionRepo;
import com.identity.service.IPermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService {
    PermissionRepo permissionRepo;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionReq permissionReq) {
        if (permissionRepo.findById(permissionReq.getName()).isPresent())
            throw new ServiceException(ErrorCode.PERMISSION_3001);
        Permission permission = permissionMapper.toPermissionFromPermissionReq(permissionReq);
        return permissionMapper.toPermissionResFromPermission(permissionRepo.save(permission));
    }

    @Override
    public void deletePermission(String permission) {
        permissionRepo.findById(permission)
                .orElseThrow(() -> new ServiceException(ErrorCode.PERMISSION_3002));
        permissionRepo.deleteById(permission);
    }

    @Override
    public PermissionResponse getPermission(String permission) {
        Permission permissionExisted = permissionRepo.findById(permission)
                .orElseThrow(() -> new ServiceException(ErrorCode.PERMISSION_3002));
        return permissionMapper.toPermissionResFromPermission(permissionExisted);
    }

    @Override
    public List<PermissionResponse> getAllPermission() {
        var permissions = permissionRepo.findAll();
        return permissions.stream()
                .map(permissionMapper::toPermissionResFromPermission).toList();
    }
}
