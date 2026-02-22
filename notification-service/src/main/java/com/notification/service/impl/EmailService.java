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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService implements IEmailService {

    MailEventClient mailEventClient;
    String apiKey = "xxx";

    @Override
    public EmailResponse sendEmail(SendEmailRequest request) {
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

        try {
            return mailEventClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new ServiceException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
