package com.chat.service;

import com.chat.dto.request.ConversationRequest;
import com.chat.dto.response.ConversationResponse;

import java.util.List;

public interface IConversationService {
    ConversationResponse create(ConversationRequest request);

    List<ConversationResponse> myConversations();
}
