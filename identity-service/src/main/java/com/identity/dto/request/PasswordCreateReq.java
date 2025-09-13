package com.identity.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordCreateReq {
    @Size(min = 6, message = "Invalid password")
    private String password;
}
