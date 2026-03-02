package com.file.service.impl;

import com.file.dto.FileInfo;
import com.file.dto.response.FileInfoResponse;
import com.file.entity.FileManagement;
import com.file.mapper.FileManagementMapper;
import com.file.repo.FileManagementRepo;
import com.file.repo.FileRepo;
import com.file.service.IFileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService implements IFileService {

    FileManagementMapper mapper;
    FileRepo fileRepo;
    FileManagementRepo fileManagementRepo;

    @Override
    public FileInfoResponse uploadFile(MultipartFile file) {
        var fileStoredInfo = fileRepo.store(file);
        FileManagement fileManagement = mapper.toFileManagement(fileStoredInfo);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        fileManagement.setOwnerId(userId);
        fileManagementRepo.save(fileManagement);

        return FileInfoResponse.builder()
                .originalName(file.getOriginalFilename())
                .url(fileStoredInfo.getUrl())
                .build();
    }
}
