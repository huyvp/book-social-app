package com.chat.service;

import com.chat.dto.request.MessageRequest;
import com.chat.dto.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IMessageService {
    MessageResponse create(MessageRequest messageRequest) throws JsonProcessingException;

    List<MessageResponse> getMessages(String conversationId);
}
