package com.chat.client;

import com.chat.dto.response.DefaultResponse;
import com.chat.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${services.profile.url}")
public interface ProfileClient {
    @GetMapping("/internal/users/{userId}")
    DefaultResponse<UserProfileResponse> getProfile(@PathVariable String userId);
}
