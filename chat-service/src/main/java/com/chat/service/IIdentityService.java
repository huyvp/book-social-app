package com.chat.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface IIdentityService {
    boolean checkToken(String token);
}
