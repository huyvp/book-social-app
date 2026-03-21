package com.identity.mapper;

import com.identity.dto.request.PermissionReq;
import com.identity.dto.response.PermissionResponse;
import com.identity.entity.Permission;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermissionFromPermissionReq(PermissionReq permissionReq);

    PermissionResponse toPermissionResFromPermission(Permission permission);
}
