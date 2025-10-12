package com.profile.controller;

import com.profile.dto.request.UserProfileReq;
import com.profile.dto.response.UserProfileResponse;
import com.profile.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileController {
    IUserProfileService userProfileService;

    @PostMapping
    UserProfileResponse createUserProfile(@RequestBody UserProfileReq userProfileReq) {
        log.info("Creating user profile : {}", userProfileReq);
        return userProfileService.createProfile(userProfileReq);
    }

    @GetMapping("/{id}")
    UserProfileResponse getUserProfile(@PathVariable("id") String id) {
        return userProfileService.getProfile(id);
    }

    @GetMapping
    List<UserProfileResponse> getUserProfiles() {
        return userProfileService.getUserProfiles();
    }
}
