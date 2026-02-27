package com.profile.controller;


import com.profile.dto.request.UserProfileReq;
import com.profile.handler.ResponseHandler;
import com.profile.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProfileController {
    IUserProfileService profileService;

    @PostMapping("/users")
    ResponseEntity<Object> createUserProfile(@RequestBody UserProfileReq userProfileReq) {
        return ResponseHandler.execute(
                profileService.createProfile(userProfileReq)
        );
    }

    @GetMapping(value = "/users/{userId}")
    ResponseEntity<Object> getUserProfile(@PathVariable("userId") String userId) {
        return ResponseHandler.execute(
                profileService.getProfileByUserId(userId)
        );
    }
}