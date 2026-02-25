package com.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ErrorCode {
    UNCATEGORIZED(5000, HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized exception"),
    // ----------------------------------
    // Related to AUTH
    // ----------------------------------
    AUTH_4000(4000, HttpStatus.FORBIDDEN, "Forbidden"),
    AUTH_4001(4001, HttpStatus.BAD_REQUEST, "Access Denied"),
    AUTH_4002(4002, HttpStatus.UNAUTHORIZED, "username or password is require"),
    AUTH_4003(4003, HttpStatus.UNAUTHORIZED, "Bad Credentials"),
    AUTH_4004(4004, HttpStatus.UNAUTHORIZED, "Invalid token"),
    // ----------------------------------
    // Related to USER
    // ----------------------------------
    USER_3001(3001, HttpStatus.BAD_REQUEST, "User existed"),
    USER_3002(3002, HttpStatus.NOT_FOUND, "User not found"),
    USER_3003(3002, HttpStatus.NOT_FOUND, "Password existed. Can't create new."),

    CANNOT_SEND_EMAIL(4000, HttpStatus.BAD_REQUEST, "Cannot send email"),;


    private int code;
    private HttpStatus httpStatus;
    private String message;
}
