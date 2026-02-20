package com.gateway.client;

import com.gateway.dto.response.DefaultResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@Repository
public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
    Mono<DefaultResponse<Boolean>> introspect(@RequestPart("token") String token);
}
