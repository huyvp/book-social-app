package com.profile.mapper;


import com.profile.dto.request.UpdateProfileRequest;
import com.profile.dto.request.UserProfileRequest;
import com.profile.dto.response.UserProfileResponse;
import com.profile.entity.UserProfile;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileRequest userProfileRequest);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);

    void update(@MappingTarget UserProfile entity, UpdateProfileRequest request);
}
