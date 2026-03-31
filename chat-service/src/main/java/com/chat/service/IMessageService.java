package com.chat.service;

import com.chat.dto.request.MessageRequest;
import com.chat.dto.response.MessageResponse;

import java.util.List;

public interface IMessageService {
    MessageResponse create(MessageRequest messageRequest);

    List<MessageResponse> getMessages(String conversationId);
}
