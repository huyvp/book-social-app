package com.notification.service.impl;

import com.notification.client.MailEventClient;
import com.notification.dto.request.EmailRequest;
import com.notification.dto.request.SendEmailRequest;
import com.notification.dto.request.Sender;
import com.notification.dto.response.EmailResponse;
import com.notification.exception.ErrorCode;
import com.notification.exception.ServiceException;
import com.notification.service.IEmailService;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService implements IEmailService {

    MailEventClient mailEventClient;

    @Value("${notification.email.brevo-api-key}")
    @NonFinal
    String apiKey;

    @Override
    public EmailResponse sendEmail(SendEmailRequest request) {
        log.info("email:send - start processing");
        log.info("email:send - to: {}", request.getTo());
        EmailRequest emailRequest = EmailRequest.builder()
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .to(List.of(request.getTo()))
                .sender(Sender.builder()
                        .email("nvh1892kw@gmail.com")
                        .name("Maekar")
                        .build()
                )
                .build();

        EmailResponse response;
        try {
            response = mailEventClient.sendEmail(apiKey, emailRequest);
            log.info("email:send - send email successful");
        } catch (FeignException e) {
            log.info("email:send - send email failed");
            throw new ServiceException(ErrorCode.CANNOT_SEND_EMAIL);
        }
        log.info("email:send - end processing");
        return response;
    }
}
