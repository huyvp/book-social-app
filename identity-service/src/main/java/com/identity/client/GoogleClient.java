package com.identity.client;

import com.identity.dto.request.ExchangeTokenReq;
import com.identity.dto.response.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "google-identity", url = "https://oauth2.googleapis.com")
public interface GoogleClient {
    @PostMapping(path = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenReq request);
}
