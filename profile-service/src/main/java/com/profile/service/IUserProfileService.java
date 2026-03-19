package com.profile.service;

import com.profile.dto.request.UpdateProfileRequest;
import com.profile.dto.request.UserProfileRequest;
import com.profile.dto.response.UserProfileResponse;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserProfileService {

    // INTERNAL
    UserProfileResponse createProfile(UserProfileRequest request);

    UserProfileResponse getProfileByUserId(String userId);

    // PUBLIC
    UserProfileResponse getProfile(String profileId);

    List<UserProfileResponse> getUserProfiles(String search);

    UserProfileResponse getMyProfile();

    UserProfileResponse updateMyProfile(UpdateProfileRequest request);

    UserProfileResponse updateAvatar(MultipartFile file);
}
