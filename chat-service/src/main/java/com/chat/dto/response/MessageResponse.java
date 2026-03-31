package com.chat.dto.response;

import com.chat.entity.ParticipantInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MessageResponse {
    String id;
    String conversationId;
    String message;
    boolean me;
    ParticipantInfo sender;
    Instant createdDate;
}
