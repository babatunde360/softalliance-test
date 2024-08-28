package com.babatunde.employee.exception;

public class ApiRequestUnauthorizedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "You are not authorized to perform this action";

    public ApiRequestUnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public ApiRequestUnauthorizedException(String message) {
        super(message);
    }

    public ApiRequestUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
