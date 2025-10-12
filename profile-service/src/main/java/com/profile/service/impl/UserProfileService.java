package com.profile.service.impl;

import com.profile.dto.request.UserProfileReq;
import com.profile.dto.response.UserProfileResponse;
import com.profile.entity.UserProfile;
import com.profile.mapper.UserProfileMapper;
import com.profile.repo.UserProfileRepo;
import com.profile.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService implements IUserProfileService {
    UserProfileRepo userProfileRepo;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(UserProfileReq request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepo.save(userProfile);
        return userProfileMapper.toUserProfileRes(userProfile);
    }

    @Override
    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return userProfileMapper.toUserProfileRes(userProfile);
    }

    @Override
    public List<UserProfileResponse> getUserProfiles() {
        List<UserProfile> userProfiles = userProfileRepo.findAll();
        if (!CollectionUtils.isEmpty(userProfiles)) {
            return userProfiles.stream().map(userProfileMapper::toUserProfileRes).toList();
        }
        return List.of();
    }

}
