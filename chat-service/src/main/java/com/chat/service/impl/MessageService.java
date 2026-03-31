package com.chat.service.impl;

import com.chat.dto.request.MessageRequest;
import com.chat.dto.response.MessageResponse;
import com.chat.mapper.MessageMapper;
import com.chat.repo.ConversationRepo;
import com.chat.repo.MessageRepo;
import com.chat.service.IMessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService implements IMessageService {
    ConversationRepo conversationRepo;
    MessageRepo messageRepo;
    MessageMapper mapper;

    @Override
    public MessageResponse create(MessageRequest messageRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        conversationRepo.findById(messageRequest.getConversationId())
                .orElseThrow()
        return null;
    }

    @Override
    public List<MessageResponse> getMessages(String conversationId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return List.of();
    }
}
