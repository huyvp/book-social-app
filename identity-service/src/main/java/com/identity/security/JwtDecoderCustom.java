package com.identity.security;

import com.identity.service.IAuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtDecoderCustom implements JwtDecoder {
    IAuthService authService;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String signerKey;
    @NonFinal
    private NimbusJwtDecoder nimbusJwtDecoder = null;


    @Override
    public Jwt decode(String token) {
        authService.introspect(token);

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), MacAlgorithm.HS256.getName());
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
