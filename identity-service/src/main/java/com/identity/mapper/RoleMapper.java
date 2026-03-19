package com.identity.mapper;

import com.identity.dto.request.RoleReq;
import com.identity.dto.response.RoleResponse;
import com.identity.entity.Permission;
import com.identity.entity.Role;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRoleFormRoleReq(RoleReq roleReq);

    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "mapPermissionsToStrings")
    RoleResponse toRoleResFromRole(Role role);

    @Named("mapPermissionsToStrings")
    default Set<String> mapPermissionsToStrings(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream().map(Permission::getName)
                .collect(Collectors.toSet());
    }
}
