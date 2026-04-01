package com.chat.controller;

import com.chat.dto.request.ConversationRequest;
import com.chat.handler.ResponseHandler;
import com.chat.service.IConversationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    IConversationService conversationService;

    @PostMapping(value = "conversations")
    ResponseEntity<Object> create(@RequestBody ConversationRequest request) {
        log.info("conversation:create - started");
        return ResponseHandler.execute(
                conversationService.create(request)
        );
    }

    @GetMapping(value = "/my-conversations")
    ResponseEntity<Object> myConversations() {
        return ResponseHandler.execute(
                conversationService.myConversations()
        );
    }
}
