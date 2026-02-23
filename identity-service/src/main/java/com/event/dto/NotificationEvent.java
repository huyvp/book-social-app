package com.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEvent {
    String channel; // zalo, email, sms...
    String recipient;
    String templateCode;
    Map<String, Object> param;
    String subject;
    String body;
}
