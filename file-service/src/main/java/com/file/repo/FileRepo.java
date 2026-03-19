package com.file.repo;

import com.file.dto.FileInfo;
import com.file.entity.FileManagement;
import com.file.exception.ErrorCode;
import com.file.exception.ServiceException;
import com.file.util.FileUtils;

import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Repository
public class FileRepo {
    @Value("${application.file.upload-dir}")
    @NonFinal
    private String uploadDir;

    @Value("${application.file.download-prefix}")
    @NonFinal
    private String downloadPrefix;

    public FileInfo store(MultipartFile file) {
        try {
            // Build folder path from configuration and user home
            Path folder = Paths.get(System.getProperty("user.home"), uploadDir);
            FileUtils.createDirectoryIfNotExists(folder.toString());

            // Extract file extension and generate unique filename
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = FileUtils.generateUniqueFileNameWithExtension(fileExtension);

            // Resolve target file path
            Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Validate file was written successfully
            if (!FileUtils.isFileValid(filePath.toString())) {
                log.warn("File was written but cannot be validated: {}", filePath);
            }

            // Build download URL
            String downloadUrl = downloadPrefix + fileName;

            // Build and return response with complete file information
            return FileInfo.builder()
                    .name(fileName)
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .md5Checksum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                    .path(folder.toString())
                    .url(downloadUrl)
                    .build();

        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new ServiceException(ErrorCode.UNCATEGORIZED);
        }
    }

    public Resource read(FileManagement fileManagement) {
        try {
            Path filePath = Paths.get(fileManagement.getPath(), fileManagement.getId());
            log.info("file:download:path {}", filePath);
            return new ByteArrayResource(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new ServiceException(ErrorCode.FILE_PATH_NOT_FOUND);
        }
    }
}
