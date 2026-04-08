package com.chat.client;

import com.chat.configuration.FeignLoggingConfig;
import com.chat.dto.response.DefaultResponse;
import com.chat.dto.response.IntrospectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "identity-client",
        url = "${services.identity.url}",
        configuration = FeignLoggingConfig.class
)
public interface IdentityClient {
    @PostMapping(value = "auth/introspect")
    DefaultResponse<IntrospectResponse> introspect(@RequestParam("token") String token);
}
