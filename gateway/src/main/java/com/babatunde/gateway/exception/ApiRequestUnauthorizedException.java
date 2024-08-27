package com.babatunde.gateway.exception;

public class ApiRequestUnauthorizedException extends RuntimeException {

    public ApiRequestUnauthorizedException(String message) {
        super(message);
    }

    public ApiRequestUnauthorizedException(String message, Throwable cause){super(message, cause);}
}