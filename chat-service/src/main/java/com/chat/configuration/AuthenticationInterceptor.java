package com.chat.configuration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Slf4j
public class AuthenticationInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String tokenStr;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            tokenStr = attributes.getRequest().getHeader("Authorization");
            log.info("Request header: {}", tokenStr);
            if (tokenStr == null || !tokenStr.startsWith("Bearer ")) tokenStr = "";
        } else {
            tokenStr = "";
        }

        requestTemplate.header("Authorization", tokenStr);
    }
}
