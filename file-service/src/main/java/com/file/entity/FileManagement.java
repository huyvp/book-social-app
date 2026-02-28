package com.file.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Getter
@Setter
@Builder
@Document(value = "fileManagement")
@AllArgsConstructor
public class FileManagement {
    @MongoId
    String id;
    String userId;
    String username;
    String givenName;
    String familyName;
    String email;
    String phoneNumber;
    String address;
    String city;
}
