package com.profile.controller;

import com.profile.dto.request.UpdateProfileRequest;
import com.profile.handler.ResponseHandler;
import com.profile.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/my-profile")
    ResponseEntity<Object> updateMyProfile(@RequestBody UpdateProfileRequest request) {
        return ResponseHandler.execute(
                userProfileService.updateMyProfile(request)
        );
    }

    @PutMapping("/avatar")
    ResponseEntity<Object> updateAvatar(@RequestParam("file") MultipartFile file) {
        return ResponseHandler.execute(
                userProfileService.updateAvatar(file)
        );
    }

    @GetMapping(value = "/search")
    ResponseEntity<Object> searchUserProfile(@RequestParam("keyword") String keyword) {
        return ResponseHandler.execute(
                userProfileService.getUserProfiles(keyword)
        );
    }
}
