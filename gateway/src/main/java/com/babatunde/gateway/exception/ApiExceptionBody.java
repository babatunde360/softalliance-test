package com.babatunde.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiExceptionBody {

    private Object message;

    private boolean success;

    private HttpStatus httpStatus;

    private Date timestamp;
}
