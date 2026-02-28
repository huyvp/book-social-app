package com.profile.controller;

import com.profile.dto.response.UserProfileResponse;
import com.profile.handler.ResponseHandler;
import com.profile.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    IUserProfileService userProfileService;

    @GetMapping("/{profileId}")
    ResponseEntity<Object> getUserProfile(@PathVariable("profileId") String profileId) {
        return ResponseHandler.execute(
                userProfileService.getProfile(profileId)
        );
    }

    @GetMapping("/my-profile")
    ResponseEntity<Object> getMyProfile() {
        return ResponseHandler.execute(
                userProfileService.getMyProfile()
        );
    }

    @GetMapping
    List<UserProfileResponse> getUserProfiles() {
        return userProfileService.getUserProfiles();
    }
}
