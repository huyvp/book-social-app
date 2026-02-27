package com.post.client;

import com.post.dto.response.DefaultResponse;
import com.post.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "profile-service",
        url = "${service.profile.url}"
)
public interface ProfileClient {
    @GetMapping(value = "/internal/users/{userId}")
    DefaultResponse<UserProfileResponse> getProfile(@PathVariable String userId);
}
