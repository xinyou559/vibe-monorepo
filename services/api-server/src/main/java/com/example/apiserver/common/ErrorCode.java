package com.example.apiserver.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200, "Success"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    
    USER_NOT_FOUND(1001, "User not found"),
    BALANCE_INSUFFICIENT(1002, "Balance insufficient"),
    ORDER_STATUS_ERROR(1003, "Order status error"),
    SERVICE_NOT_FOUND(1004, "Service not found"),
    SERVICE_UNAVAILABLE(1005, "Service unavailable");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
