package com.identity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileReq {
    String userId;
    String givenName;
    String familyName;
    String email;
    String address;
    String phoneNumber;
    String city;
}
