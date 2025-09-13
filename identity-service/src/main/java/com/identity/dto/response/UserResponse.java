package com.identity.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String givenName;
    String familyName;
    String email;
    String avatar;
    String phoneNumber;
    String address;
    Boolean noPassword;
    String role;
    Set<String> roles;
}
