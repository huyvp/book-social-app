package com.profile.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileReq {
    String userId;
    String givenName;
    String familyName;
    String email;
    String phoneNumber;
    String address;
}