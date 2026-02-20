package com.profile.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.profile.dto.response.DefaultResponse;
import com.profile.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDateTime;

public class SecurityExceptionHandler {
    public static void builder(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.resetBuffer();
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        DefaultResponse<?> defaultResponse = DefaultResponse.builder()
                .code(errorCode.getCode())
                .timestamp(LocalDateTime.now())
                .message(errorCode.getMessage())
                .status(errorCode.getHttpStatus())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String body = objectMapper.writeValueAsString(defaultResponse);
        response.getWriter().write(body);
        response.flushBuffer();
    }
}
