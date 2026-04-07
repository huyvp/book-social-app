package com.chat.service.impl;

import com.chat.client.IdentityClient;
import com.chat.service.IIdentityService;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService implements IIdentityService {
    IdentityClient identityClient;

    @Override
    public boolean checkToken(String token) {
        try {
            return identityClient.introspect(token).getResult();
        } catch (FeignException e) {
            log.error("introspect error: {}", e.getMessage());
            return false;
        }
    }
}
