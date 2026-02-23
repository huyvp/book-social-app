package com.notification.controller;

import com.notification.dto.request.SendEmailRequest;
import com.notification.handler.ResponseHandler;
import com.notification.service.IEmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    IEmailService emailService;

    @PostMapping(path = "/email/send")
    public ResponseEntity<Object> sendMail(@RequestBody SendEmailRequest request) {
        return ResponseHandler.execute(
                emailService.sendEmail(request)
        );
    }
}
