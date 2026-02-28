package com.profile.controller;


import com.profile.dto.request.UserProfileReq;
import com.profile.handler.ResponseHandler;
import com.profile.service.IUserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProfileController {
    IUserProfileService profileService;

    @PostMapping("/users")
    ResponseEntity<Object> createUserProfile(@RequestBody UserProfileReq userProfileReq) {
        log.info("profile:internal:createUserProfile - started");
        return ResponseHandler.execute(
                profileService.createProfile(userProfileReq)
        );
    }

    @GetMapping(value = "/users/{userId}")
    ResponseEntity<Object> getUserProfile(@PathVariable("userId") String userId) {
        log.info("profile:internal:getUserProfileByUserId - started");
        return ResponseHandler.execute(
                profileService.getProfileByUserId(userId)
        );
    }
}