package com.notification.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.notification.dto.response.DefaultResponse;
import com.notification.exception.ErrorCode;
import com.notification.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<Object> handleRestException(Exception e) {
        log.error(e.getMessage(), e);
        DefaultResponse<Object> response = DefaultResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.UNCATEGORIZED.getCode())
                .status(ErrorCode.UNCATEGORIZED.getHttpStatus())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    ResponseEntity<Object> handleServiceException(ServiceException e) {
        DefaultResponse<Object> response = DefaultResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(e.getErrorCode().getCode())
                .status(e.getErrorCode().getHttpStatus())
                .message(e.getErrorCode().getMessage())
                .build();
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseEntity<Object> handleAccessDeniedException() {
        DefaultResponse<Object> appResponse = DefaultResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.AUTH_4000.getCode())
                .status(ErrorCode.AUTH_4000.getHttpStatus())
                .message(ErrorCode.AUTH_4000.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.AUTH_4000.getHttpStatus()).body(appResponse);
    }
}
