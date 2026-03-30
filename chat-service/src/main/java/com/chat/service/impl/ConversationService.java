package com.chat.service.impl;

import com.chat.client.ProfileClient;
import com.chat.dto.request.CreateConversationRequest;
import com.chat.dto.response.ConversationResponse;
import com.chat.entity.Conversation;
import com.chat.entity.ParticipantInfo;
import com.chat.exception.ErrorCode;
import com.chat.exception.ServiceException;
import com.chat.mapper.ConversationMapper;
import com.chat.repo.ConversationRepo;
import com.chat.service.IConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService implements IConversationService {

    ConversationMapper mapper;
    ProfileClient profileClient;
    ConversationRepo conversationRepo;

    @Override
    public ConversationResponse create(CreateConversationRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var userInfoResponse = profileClient.getProfile(userId);
        log.info("conversation:create:profile - {}", userInfoResponse);

        var participantInfoResponse = profileClient.getProfile(request.getParticipantIds().get(0));
        log.info("conversation:create:participant - {}", request.getParticipantIds());

        if (Objects.isNull(participantInfoResponse) || Objects.isNull(userInfoResponse)) {
            throw new ServiceException(ErrorCode.USER_NOT_FOUND);
        }

        var userInfo = userInfoResponse.getResult();
        var participantInfo = participantInfoResponse.getResult();

        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.add(participantInfo.getUserId());

        var sortedIds = userIds.stream().sorted().toList();
        String userIdsHash = generateParticipantHash(sortedIds);


        var conversation = conversationRepo.findByParticipantsHash(userIdsHash)
                .orElseGet(() -> {
                    List<ParticipantInfo> participantInfoList = List.of(
                            ParticipantInfo.builder()
                                    .userId(userId)
                                    .username(userInfo.getUsername())
                                    .firstName(userInfo.getGivenName())
                                    .lastName(userInfo.getFamilyName())
                                    .avatar(userInfo.getAvatar())
                                    .build(),
                            ParticipantInfo.builder()
                                    .userId(participantInfo.getUserId())
                                    .username(participantInfo.getUsername())
                                    .firstName(participantInfo.getGivenName())
                                    .lastName(participantInfo.getFamilyName())
                                    .avatar(participantInfo.getAvatar())
                                    .build()
                    );

                    Conversation newConversation = Conversation.builder()
                            .type(request.getType())
                            .participants(participantInfoList)
                            .participantsHash(userIdsHash)
                            .createdDate(Instant.now())
                            .modifiedDate(Instant.now())
                            .build();
                    return conversationRepo.save(newConversation);
                });

        return toConversationResponse(conversation);
    }

    @Override
    public List<ConversationResponse> myConversations() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Conversation> conversations = conversationRepo.findAllByParticipantIdsContains(userId);
        return conversations.stream().map(this::toConversationResponse).toList();
    }

    private String generateParticipantHash(List<String> ids) {
        StringJoiner stringJoiner = new StringJoiner("_");
        ids.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        ConversationResponse conversationResponse = mapper.toConversationResponse(conversation);

        conversation.getParticipants().stream()
                .filter(participantInfo -> !participantInfo.getUserId().equals(currentUserId))
                .findFirst().ifPresent(participantInfo -> {
                    conversationResponse.setConversationName(participantInfo.getUsername());
                    conversationResponse.setConversationAvatar(participantInfo.getAvatar());
                });

        return conversationResponse;
    }
}
