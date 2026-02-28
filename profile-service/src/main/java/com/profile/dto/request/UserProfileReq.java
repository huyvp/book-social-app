package com.profile.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileReq {
    String userId;
    String username;
    String givenName;
    String familyName;
    String email;
    String phoneNumber;
    String address;
}