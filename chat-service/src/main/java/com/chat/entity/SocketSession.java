package com.chat.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "socket_session")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocketSession {

    @MongoId
    String id;

    String sessionId;

    @NotBlank
    String userId;

    Instant createdAt;
}
