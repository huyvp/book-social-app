package com.profile.service.impl;

import static com.profile.exception.ErrorCode.PROFILE_NOT_FOUND;
import static com.profile.exception.ErrorCode.USER_NOT_FOUND;

import com.profile.client.FileClient;
import com.profile.dto.request.UpdateProfileRequest;
import com.profile.dto.request.UserProfileRequest;
import com.profile.dto.response.UserProfileResponse;
import com.profile.entity.UserProfile;
import com.profile.exception.ServiceException;
import com.profile.mapper.UserProfileMapper;
import com.profile.repo.UserProfileRepo;
import com.profile.service.IUserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService implements IUserProfileService {

    UserProfileRepo userProfileRepo;
    UserProfileMapper userProfileMapper;
    FileClient fileClient;

    public UserProfileResponse createProfile(UserProfileRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepo.save(userProfile);
        log.info("profile:internal:createUserProfile - success");
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileResponse getProfileByUserId(String userId) {
        UserProfile userProfile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new ServiceException(PROFILE_NOT_FOUND));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public List<UserProfileResponse> getUserProfiles(String search) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<UserProfile> userProfiles = userProfileRepo.findAllByUsernameLike(search);
        return userProfiles.stream()
                .filter(userProfile -> !userId.equals(userProfile.getUserId()))
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }

    @Override
    public UserProfileResponse getMyProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        return userProfileMapper.toUserProfileResponse(profile);
    }

    @Override
    public UserProfileResponse updateMyProfile(UpdateProfileRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        userProfileMapper.update(profile, request);

        var savedProfile = userProfileRepo.save(profile);

        return userProfileMapper.toUserProfileResponse(savedProfile);
    }

    @Override
    public UserProfileResponse updateAvatar(MultipartFile file) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        var fileResponse = fileClient.uploadFile(file);
        profile.setAvatar(fileResponse.getResult().getUrl());

        return userProfileMapper.toUserProfileResponse(profile);
    }

}
