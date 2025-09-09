package com.identity.service.impl;

import com.identity.client.GoogleClient;
import com.identity.dto.request.ExchangeTokenReq;
import com.identity.dto.request.UserLogin;
import com.identity.entity.InvalidatedToken;
import com.identity.entity.Role;
import com.identity.entity.User;
import com.identity.exception.ErrorCode;
import com.identity.exception.ServiceException;
import com.identity.repo.InvalidatedTokenRepo;
import com.identity.repo.UserRepo;
import com.identity.service.IAuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService implements IAuthService {
    UserRepo userRepo;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepo invalidatedToken;
    InvalidatedTokenRepo invalidatedTokenRepo;
    GoogleClient googleClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_ABLE_DURATION;
    @NonFinal
    @Value("${google.client-id}")
    protected String GOOGLE_CLIENT_ID;
    @NonFinal
    @Value("${google.client-secret}")
    protected String GOOGLE_CLIENT_SECRET;
    @NonFinal
    @Value("${google.redirect-uri}")
    protected String REDIRECT_URI;
    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    @Override
    public String login(UserLogin userLogin) {
        String username = userLogin.getUsername();
        String password = userLogin.getPassword();

        if (username == null || password == null)
            throw new ServiceException(ErrorCode.AUTH_4002);
        User user = userRepo.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new ServiceException(ErrorCode.AUTH_4003));
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new ServiceException(ErrorCode.AUTH_4003);

        return generateToken(user);
    }

    @Override
    public void logout(String token) {
        try {
            SignedJWT signedToken = verifyToken(token, true);
            String tokenId = signedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(tokenId)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepo.save(invalidatedToken);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String refreshToken(String token) {
        SignedJWT signedToken = verifyToken(token, true);
        try {
            String tokenId = signedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(tokenId)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepo.save(invalidatedToken);
            String username = signedToken.getJWTClaimsSet().getSubject();

            User user = userRepo.findByUsernameAndActiveTrue(username)
                    .orElseThrow(() -> new ServiceException(ErrorCode.AUTH_4001));
            return generateToken(user);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean introspect(String token) {
        verifyToken(token, false);
        return true;
    }

    @Override
    public String outboundAuth(String code) {
        var response = googleClient.exchangeToken(ExchangeTokenReq.builder()
                .clientId(GOOGLE_CLIENT_ID)
                .clientSecret(GOOGLE_CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .code(code)
                .build()
        );
        log.info("TOKEN RESPONSE {}", response);

        return response.getAccessToken();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) {
        boolean verified;
        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expireTime = isRefresh
                    ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESH_ABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                    : signedJWT.getJWTClaimsSet().getExpirationTime();
            verified = signedJWT.verify(verifier);

            if (!verified && expireTime.after(new Date()))
                throw new ServiceException(ErrorCode.AUTH_4004);
            if (invalidatedToken.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
                throw new ServiceException(ErrorCode.AUTH_4004);
            return signedJWT;
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("nvh189")
                .issueTime(new Date())
                .claim("scope", scopeBuilder(user.getRoles()))
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String scopeBuilder(Set<Role> roles) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!roles.isEmpty()) {
            roles.forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!role.getPermissions().isEmpty()) {
                    role.getPermissions().forEach(
                            permission -> stringJoiner.add(permission.getName())
                    );
                }
            });
        }
        return stringJoiner.toString();
    }
}
