package com.gateway.service;

import com.gateway.client.IdentityClient;
import com.gateway.dto.response.DefaultResponse;

import com.gateway.dto.response.IntrospectResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {

    IdentityClient identityClient;

    public Mono<DefaultResponse<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(token);
    }
}
