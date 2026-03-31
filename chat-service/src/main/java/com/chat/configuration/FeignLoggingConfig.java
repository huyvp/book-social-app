package com.chat.configuration;

import org.springframework.context.annotation.Bean;

import feign.Logger;

public class FeignLoggingConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    Logger feignLogger() {
        return new CustomFeignLogger();
    }
}
