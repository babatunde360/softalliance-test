package com.babatunde.employee.exception;

public class ApiResourceTakenException  extends RuntimeException {

    public ApiResourceTakenException(String message) {
        super(message);
    }

    public ApiResourceTakenException(String message, Throwable cause){super(message, cause);}
}
