package com.profile.client;

import com.profile.configuration.AuthenticationInterceptor;
import com.profile.dto.response.DefaultResponse;
import com.profile.dto.response.FileInfoResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "file-service",
        url = "${service.file.url}",
        configuration = {AuthenticationInterceptor.class}
)
public interface FileClient {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    DefaultResponse<FileInfoResponse> uploadFile(@RequestPart(name = "file") MultipartFile file);
}
