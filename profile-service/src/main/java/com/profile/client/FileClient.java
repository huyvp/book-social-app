package com.profile.client;

import com.profile.configuration.AuthenticationInterceptor;
import com.profile.dto.response.FileInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "file-service",
        url = "${service.file.url}",
        configuration = {AuthenticationInterceptor.class}
)
public interface FileClient {
    @PostMapping(value = "/upload")
    FileInfoResponse uploadFile(@RequestPart(name = "file") MultipartFile file);
}
