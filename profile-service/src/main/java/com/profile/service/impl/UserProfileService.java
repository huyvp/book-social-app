package com.profile.service.impl;

import com.profile.client.FileClient;
import com.profile.dto.request.UserProfileReq;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.profile.exception.ErrorCode.PROFILE_NOT_FOUND;
import static com.profile.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService implements IUserProfileService {

    UserProfileRepo userProfileRepo;
    UserProfileMapper userProfileMapper;
    FileClient fileClient;

    public UserProfileResponse createProfile(UserProfileReq request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepo.save(userProfile);
        log.info("profile:internal:createUserProfile - success");
        return userProfileMapper.toUserProfileRes(userProfile);
    }

    @Override
    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile = userProfileRepo.findById(id)
                .orElseThrow(() -> new ServiceException(PROFILE_NOT_FOUND));
        return userProfileMapper.toUserProfileRes(userProfile);
    }

    @Override
    public UserProfileResponse getProfileByUserId(String userId) {
        UserProfile userProfile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));
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

    @Override
    public UserProfileResponse getMyProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        return userProfileMapper.toUserProfileRes(profile);
    }

    @Override
    public UserProfileResponse updateAvatar(MultipartFile file) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        var profile = userProfileRepo.findByUserId(userId)
                .orElseThrow(() -> new ServiceException(USER_NOT_FOUND));

        var fileResponse = fileClient.uploadFile(file);
        profile.setAvatar(fileResponse.getUrl());

        return userProfileMapper.toUserProfileRes(profile);
    }

}
