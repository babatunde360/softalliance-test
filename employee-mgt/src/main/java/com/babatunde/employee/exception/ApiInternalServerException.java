package com.babatunde.employee.exception;

public class ApiInternalServerException extends RuntimeException{

    public static final String DEFAULT_MESSAGE = "Oops an error occurred while processing your request";

    public ApiInternalServerException() {
        super(DEFAULT_MESSAGE);
    }

    public ApiInternalServerException(String message) {
        super(message);
    }

    public ApiInternalServerException(String message, Throwable cause){super(message, cause);}
}
