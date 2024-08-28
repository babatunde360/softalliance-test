package com.babatunde.employee.exception;

public class ApiAccessForbiddenException extends RuntimeException {

    public ApiAccessForbiddenException(String message) {
        super(message);
    }

    public ApiAccessForbiddenException(String message, Throwable cause){super(message, cause);}
}