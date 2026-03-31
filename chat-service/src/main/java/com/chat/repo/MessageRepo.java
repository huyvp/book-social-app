package com.chat.repo;

import com.chat.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepo extends MongoRepository<Message, String> {
    List<Message> findAllByConversationIdOrderByCreatedDateDesc(String conversationId);
}
