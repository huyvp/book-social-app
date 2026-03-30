package com.chat.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

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
