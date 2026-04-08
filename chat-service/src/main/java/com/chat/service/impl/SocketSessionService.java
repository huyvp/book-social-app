package com.chat.service.impl;

import com.chat.entity.SocketSession;
import com.chat.repo.SocketSessionRepo;
import com.chat.service.ISocketSessionService;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketSessionService implements ISocketSessionService {
    SocketSessionRepo socketSessionRepo;

    @Override
    public SocketSession create(SocketSession socketSession) {
        return socketSessionRepo.save(socketSession);
    }

    @Override
    public void delete(SocketIOClient client) {
        socketSessionRepo.deleteAllBySessionId(client.getSessionId().toString());
    }
}
