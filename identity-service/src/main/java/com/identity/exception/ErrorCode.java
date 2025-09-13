package com.identity.exception;

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
    // ----------------------------------
    // Related to ROLE
    // ----------------------------------
    ROLE_3001(3001, HttpStatus.BAD_REQUEST, "Role existed"),
    ROLE_3002(3002, HttpStatus.NOT_FOUND, "Role not found"),
    // ----------------------------------
    // Related to PERMISSION
    // ----------------------------------
    PERMISSION_3001(3001, HttpStatus.BAD_REQUEST, "Permission existed"),
    PERMISSION_3002(3002, HttpStatus.NOT_FOUND, "Permission not found"),
    // ----------------------------------
    // Related to FILE
    // ----------------------------------
    FILE_2001(2001, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "File format not supported"),
    FILE_2002(2002, HttpStatus.PAYLOAD_TOO_LARGE, "Payload Too Large"),
    FILE_2003(2003, HttpStatus.INTERNAL_SERVER_ERROR, "Maximum image price is five"),
    // ----------------------------------
    // Related to VALIDATION
    // ----------------------------------
    VALID_1000(1000, HttpStatus.BAD_REQUEST, "Id must be greater than 0"),
    VALID_1001(1001, HttpStatus.BAD_REQUEST, "Full name is required"),
    VALID_1002(1002, HttpStatus.BAD_REQUEST, "Phone number is required"),
    VALID_1003(1003, HttpStatus.BAD_REQUEST, "Password is required"),
    VALID_1004(1004, HttpStatus.BAD_REQUEST, "Image url must be between 3 and 200 characters"),
    VALID_1005(1005, HttpStatus.BAD_REQUEST, "Category' name can not be empty"),
    VALID_1006(1006, HttpStatus.BAD_REQUEST, "Title must be between 3 and 200 characters"),
    VALID_1007(1007, HttpStatus.BAD_REQUEST, "Product price must be between 0 and 10.000.000"),
    VALID_1008(1008, HttpStatus.BAD_REQUEST, "Shipping method is required"),
    VALID_1009(1008, HttpStatus.BAD_REQUEST, "Shipping address is required"),
    VALID_1010(1010, HttpStatus.BAD_REQUEST, "Payment method is required"),
    VALID_1011(1011, HttpStatus.BAD_REQUEST, "Number of product must be greater than 1"),
    VALID_1012(1012, HttpStatus.BAD_REQUEST, "Shipping date must be at least today"),
    VALID_1013(1013, HttpStatus.BAD_REQUEST, "Shipping address is require"),
    VALID_1014(1014, HttpStatus.BAD_REQUEST, "Age must be at least {min}");

    private int code;
    private HttpStatus httpStatus;
    private String message;
}
