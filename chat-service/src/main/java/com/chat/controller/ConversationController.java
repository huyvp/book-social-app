package com.chat.controller;

import com.chat.dto.request.CreateConversationRequest;
import com.chat.handler.ResponseHandler;
import com.chat.service.IConversationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "conversations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
    IConversationService conversationService;

    @PostMapping
    ResponseEntity<Object> create(@RequestBody CreateConversationRequest request) {
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
