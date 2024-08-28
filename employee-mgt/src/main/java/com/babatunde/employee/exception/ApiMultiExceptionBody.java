package com.babatunde.employee.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiMultiExceptionBody {

    private Object message;

    private boolean success;

    private HttpStatus httpStatus;

    private LocalDateTime timestamp;
}
