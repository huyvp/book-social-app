package com.file.repo;

import com.file.entity.FileManagement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepo extends MongoRepository<FileManagement, String> {
    Optional<FileManagement> findByUserId(String userId);
}
