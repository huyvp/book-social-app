package com.chat.controller;


import com.chat.entity.SocketSession;
import com.chat.service.IIdentityService;
import com.chat.service.ISocketSessionService;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketHandler {
    SocketIOServer server;
    IIdentityService identityService;
    ISocketSessionService socketSessionService;

    @PostConstruct
    public void init() {
        server.start();
        server.addListeners(this);
        log.info("Socket Server started");
    }

    @OnConnect
    public void clientConnected(SocketIOClient client) {
        // Get token form request param
        String token = client.getHandshakeData().getSingleUrlParam("token");
        log.info("client token: {}", token);
        // Verify token
        var response = identityService.checkToken(token);
        if (!response.isValid()) client.disconnect();
        SocketSession session = SocketSession.builder()
                .sessionId(client.getSessionId().toString())
                .createdAt(Instant.now())
                .userId(response.getUserId())
                .build();
        socketSessionService.create(session);
        log.info("Client connected: {}", client.getSessionId());
    }

    @OnDisconnect
    public void clientDisconnected(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId());
        socketSessionService.delete(client);
    }

    @PreDestroy
    public void destroy() {
        server.stop();
        log.info("Socket Server stopped");
    }
}
