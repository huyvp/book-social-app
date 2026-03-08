package com.profile.service;

import com.profile.dto.request.UserProfileReq;
import com.profile.dto.response.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserProfileService {
    UserProfileResponse createProfile(UserProfileReq request);

    UserProfileResponse getProfile(String profileId);

    UserProfileResponse getProfileByUserId(String userId);

    List<UserProfileResponse> getUserProfiles(String search);

    UserProfileResponse getMyProfile();

    UserProfileResponse updateAvatar(MultipartFile file);
}
