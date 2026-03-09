package com.chat.service;

import com.chat.dto.request.CreateConversationRequest;
import com.chat.dto.response.ConversationResponse;

import java.util.List;

public interface IConversationService {
    ConversationResponse create(CreateConversationRequest request);

    List<ConversationResponse> myConversations();
}
