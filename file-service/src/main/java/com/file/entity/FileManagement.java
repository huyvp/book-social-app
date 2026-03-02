package com.file.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Getter
@Setter
@Builder
@Document(collection = "file_management")
@AllArgsConstructor
@NoArgsConstructor
public class FileManagement {
    @MongoId
    String id;
    String contentType;
    String size;
    String path;
    String md5Checksum;
    String ownerId;
}
