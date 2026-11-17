package com.fzwywm.common;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final int status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
    }

    public int getStatus() {
        return status;
    }

    public static ApiException badRequest(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, message);
    }

    public static ApiException tooManyRequests(String message) {
        return new ApiException(HttpStatus.TOO_MANY_REQUESTS, message);
    }
}
