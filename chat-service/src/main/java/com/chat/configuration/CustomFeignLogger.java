package com.chat.configuration;

import lombok.extern.slf4j.Slf4j;

import feign.Logger;

@Slf4j
public class CustomFeignLogger extends Logger {
    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info("[Feign] {} - {}", configKey, String.format(format, args));
    }
}
