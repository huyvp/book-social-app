package com.notification.controller;


import com.event.dto.NotificationEvent;
import com.notification.dto.request.Recipient;
import com.notification.dto.request.SendEmailRequest;
import com.notification.service.IEmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    IEmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listen(NotificationEvent event) {
        log.info("Received request {}", event);
        emailService.sendEmail(SendEmailRequest.builder()
                .htmlContent(event.getBody())
                .subject(event.getSubject())
                .to(Recipient.builder()
                        .email(event.getRecipient())
                        .build())
                .build()
        );
    }
}
