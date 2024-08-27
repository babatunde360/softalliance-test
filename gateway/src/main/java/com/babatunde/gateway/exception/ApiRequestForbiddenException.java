package com.babatunde.gateway.exception;

public class ApiRequestForbiddenException  extends RuntimeException {

    public ApiRequestForbiddenException(String message) {
        super(message);
    }

    public ApiRequestForbiddenException(String message, Throwable cause){super(message, cause);}
}