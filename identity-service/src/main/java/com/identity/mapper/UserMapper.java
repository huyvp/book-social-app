package com.identity.mapper;

import com.identity.dto.request.UserReq;
import com.identity.dto.response.UserResponse;
import com.identity.entity.Role;
import com.identity.entity.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "firstName", target = "givenName")
    @Mapping(source = "lastName", target = "familyName")
    User toUserFromUserReq(UserReq userReq);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRoleToString")
    UserResponse toUserResFromUser(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserReq userReq);

    @Named("mapRoleToString")
    default Set<String> mapRoleToString(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream().map(Role::getName)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void handleNullStrings(@MappingTarget UserResponse userResponse) {
        if (userResponse.getAddress() == null) userResponse.setAddress("");
        if (userResponse.getPhoneNumber() == null) userResponse.setPhoneNumber("");
    }
}
