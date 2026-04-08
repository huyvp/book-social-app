package com.chat.service;

import com.chat.entity.SocketSession;
import com.corundumstudio.socketio.SocketIOClient;

public interface ISocketSessionService {
    SocketSession create(SocketSession socketSession);

    void delete(SocketIOClient client);
}
