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
    UserProfileResponse getMyProfile();

    UserProfileResponse getProfile(String profileId);

    UserProfileResponse updateMyProfile(UpdateProfileRequest request);

    UserProfileResponse updateAvatar(MultipartFile file);

    List<UserProfileResponse> getUserProfiles(String search);
}
