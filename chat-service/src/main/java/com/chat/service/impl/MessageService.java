package com.chat.service.impl;

import com.chat.client.ProfileClient;
import com.chat.dto.request.MessageRequest;
import com.chat.dto.response.MessageResponse;
import com.chat.entity.Message;
import com.chat.entity.ParticipantInfo;
import com.chat.exception.ErrorCode;
import com.chat.exception.ServiceException;
import com.chat.mapper.MessageMapper;
import com.chat.repo.ConversationRepo;
import com.chat.repo.MessageRepo;
import com.chat.service.IMessageService;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService implements IMessageService {
    ConversationRepo conversationRepo;
    MessageRepo messageRepo;
    MessageMapper mapper;
    ProfileClient profileClient;
    SocketIOServer socketIOServer;

    @Override
    public MessageResponse create(MessageRequest messageRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        conversationRepo.findById(messageRequest.getConversationId())
                .orElseThrow(() -> new ServiceException(ErrorCode.CONVERSATION_NOT_FOUND))
                .getParticipants()
                .stream()
                .filter(participant -> userId.equals(participant.getUserId()))
                .findAny()
                .orElseThrow(() -> new ServiceException(ErrorCode.CONVERSATION_NOT_FOUND));

        var profileResponse = profileClient.getProfile(userId);

        if (Objects.isNull(profileResponse)) {
            throw new ServiceException(ErrorCode.UNCATEGORIZED);
        }

        var profile = profileResponse.getResult();
        Message message = mapper.toMessage(messageRequest);
        message.setSender(
                ParticipantInfo.builder()
                        .userId(profile.getUserId())
                        .username(profile.getUsername())
                        .firstName(profile.getFirstName())
                        .lastName(profile.getLastName())
                        .avatar(profile.getAvatar())
                        .build()
        );
        message.setCreatedDate(Instant.now());
        message = messageRepo.save(message);

        String msgString = message.getMessage();

        socketIOServer.getAllClients().forEach(client -> {
            client.sendEvent("message", msgString);
        });

        return mapper.toMessageResponse(message);
    }

    @Override
    public List<MessageResponse> getMessages(String conversationId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        conversationRepo.findById(conversationId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CONVERSATION_NOT_FOUND))
                .getParticipants()
                .stream()
                .filter(participantInfo -> userId.equals(participantInfo.getUserId()))
                .findAny()
                .orElseThrow(() -> new ServiceException(ErrorCode.CONVERSATION_NOT_FOUND));

        var messages = messageRepo.findAllByConversationIdOrderByCreatedDateDesc(conversationId);

        return messages.stream().map(this::toMessageResponse).toList();
    }

    private MessageResponse toMessageResponse(Message chatMessage) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var chatMessageResponse = mapper.toMessageResponse(chatMessage);
        chatMessageResponse.setMe(userId.equals(chatMessage.getSender().getUserId()));
        return chatMessageResponse;
    }
}
