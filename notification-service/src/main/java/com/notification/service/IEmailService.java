package com.notification.service;

import com.notification.dto.request.SendEmailRequest;
import com.notification.dto.response.EmailResponse;

public interface IEmailService {
    EmailResponse sendEmail(SendEmailRequest request);
}
