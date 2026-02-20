package com.profile.service;

import com.profile.dto.request.UserProfileReq;
import com.profile.dto.response.UserProfileResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface IUserProfileService {
    UserProfileResponse createProfile(UserProfileReq request);

    UserProfileResponse getProfile(String id);

    @PreAuthorize("hasRole('ADMIN')")
    List<UserProfileResponse> getUserProfiles();
}
