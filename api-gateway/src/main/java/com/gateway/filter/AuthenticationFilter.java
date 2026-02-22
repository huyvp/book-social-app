package com.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.dto.response.DefaultResponse;
import com.gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter implements Ordered, GlobalFilter {

    ObjectMapper objectMapper;
    IdentityService identityService;

    @NonFinal
    private String[] publicEndpoints = {
            "/identity/auth/.*",
            "/identity/users/registration",
            "/notification/email/send"
    };

    @NonFinal
    @Value("${service.api-prefix}")
    protected String prefix;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = exchange.getRequest().getId();
        log.info("[{}] Start authentication filter request", requestId);

        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().toString();
        // Get token from header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        log.info("[{}] Method: {} - Path: {}", requestId, method, path);
        log.info("[{}] Headers: {}", requestId, authHeader);

        if (CollectionUtils.isEmpty(authHeader)) {
            return unAuthentication(exchange.getResponse());
        }
        String token = authHeader.get(0).replace("Bearer ", "");
        log.info("[{}] Token authorization: {}", requestId, token);

        // Verify token - Delegate identity service
        return identityService.introspect(token).flatMap(res -> {
                    if (res.getResult()) return chain.filter(exchange);
                    else return unAuthentication(exchange.getResponse());
                }
        ).onErrorResume(
                throwable -> unAuthentication(exchange.getResponse())
        );

    }

    @Override
    public int getOrder() {
        return -1;
    }


    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints).anyMatch(
                s -> request.getURI().getPath().matches(prefix + s)
        );
    }

    Mono<Void> unAuthentication(ServerHttpResponse response) {
        DefaultResponse<?> unAuthResponse = DefaultResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .code(10401)
                .message("Unauthorized")
                .build();
        String responseStr;

        try {
            responseStr = objectMapper.writeValueAsString(unAuthResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(responseStr.getBytes()))
        );
    }
}
