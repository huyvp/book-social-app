package com.chat.repo;

import com.chat.entity.SocketSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocketSessionRepo extends MongoRepository<SocketSession, String> {
    void deleteAllBySessionId(String sessionId);

    List<SocketSession> findAllByUserIdIn(List<String> userIdList);
}
