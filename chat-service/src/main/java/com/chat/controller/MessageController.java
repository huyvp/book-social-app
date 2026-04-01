package com.chat.controller;

import com.chat.dto.request.MessageRequest;
import com.chat.handler.ResponseHandler;
import com.chat.service.IMessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "messages")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {
    IMessageService messageService;

    @PostMapping
    ResponseEntity<?> create(@RequestBody MessageRequest messageRequest) {
        log.info("message:create - started");
        return ResponseHandler.execute(
                messageService.create(messageRequest)
        );
    }

    @GetMapping
    ResponseEntity<?> getMessages(@RequestParam String conversationId) {
        log.info("message:getMessages - started");
        return ResponseHandler.execute(
                messageService.getMessages(conversationId)
        );
    }
}
