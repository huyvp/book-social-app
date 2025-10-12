package com.profile.service;

import com.profile.dto.request.UserProfileReq;
import com.profile.dto.response.UserProfileResponse;

import java.util.List;

public interface IUserProfileService {
    UserProfileResponse createProfile(UserProfileReq request);

    UserProfileResponse getProfile(String id);

    List<UserProfileResponse> getUserProfiles();
}
