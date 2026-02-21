package com.notification.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.notification.exception.ErrorCode;
import com.notification.handler.SecurityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationEntrypoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ErrorCode errorCode = ErrorCode.AUTH_4004;
        log.error(authException.toString());
        SecurityExceptionHandler.builder(response, errorCode);
    }
}
