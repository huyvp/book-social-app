package com.chat.service;

import com.chat.dto.response.IntrospectResponse;

public interface IIdentityService {
    IntrospectResponse checkToken(String token);
}
