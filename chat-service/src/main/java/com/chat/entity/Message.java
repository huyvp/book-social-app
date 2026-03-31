package com.chat.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "message")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @MongoId
    String id;
    String conversationId;
    String message;
    ParticipantInfo sender;
    @Indexed
    Instant createdDate;
}
