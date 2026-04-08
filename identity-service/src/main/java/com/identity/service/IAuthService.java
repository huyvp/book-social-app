package com.identity.service;

import com.identity.dto.request.UserLogin;
import com.identity.dto.response.IntrospectResponse;

public interface IAuthService {
    String login(UserLogin userLogin);

    void logout(String token);

    String refreshToken(String token);

    IntrospectResponse introspect(String token);

    String outboundAuth(String code);
}
