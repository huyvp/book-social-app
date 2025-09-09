package com.identity.controller;

import com.identity.dto.request.UserLogin;
import com.identity.handler.ResponseHandler;
import com.identity.service.IAuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class AuthController {
    IAuthService authService;

    @PostMapping("/outbound")
    public ResponseEntity<Object> outboundAuth(@RequestParam(name = "code") String code) {
        return ResponseHandler.execute(
                authService.outboundAuth(code)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLogin userLogin) {
        return ResponseHandler.execute(
                authService.login(userLogin)
        );
    }

    @PostMapping("/introspect")
    public ResponseEntity<Object> introspect(@RequestParam("token") String token) {
        return ResponseHandler.execute(
                authService.introspect(token)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@RequestParam("token") String token) {
        return ResponseHandler.execute(
                authService.refreshToken(token)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestParam("token") String token) {
        authService.logout(token);
        return ResponseHandler.execute();
    }
}
