package com.file.repo;

import com.file.entity.FileManagement;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileManagementRepo extends MongoRepository<FileManagement, String> {
}
