package com.identity.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileReq {
    String userId;
    String username;
    String firstName;
    String lastName;
    String email;
    String address;
    String phoneNumber;
    String city;
}
