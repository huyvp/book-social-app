package com.file.service;


import com.file.dto.response.FileInfoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    FileInfoResponse uploadFile(MultipartFile file);
}
