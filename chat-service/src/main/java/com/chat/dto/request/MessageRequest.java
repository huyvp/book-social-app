package com.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    @NotBlank(message = "conversationId can not be blank")
    String conversationId;
    @NotBlank(message = "message can not be blank")
    String message;
}
