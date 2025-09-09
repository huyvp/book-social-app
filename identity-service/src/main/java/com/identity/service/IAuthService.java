package com.identity.service;

import com.identity.dto.request.UserLogin;

public interface IAuthService {
    String login(UserLogin userLogin);

    void logout(String token);

    String refreshToken(String token);

    boolean introspect(String token);

    String outboundAuth(String code);
}
