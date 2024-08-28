package com.babatunde.employee.exception;

import com.babatunde.employee.service.feign.ServiceEnum;
import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
@ResponseStatus
public class ApiExceptionHandler {

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(value = {ApiBadRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiBadRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiExceptionBody apiExceptionBody = new ApiExceptionBody(e.getMessage(),
                false, badRequest, LocalDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiExceptionBody, badRequest);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(value = {ApiResourceNotFoundException.class})
    public ResponseEntity<Object> handleApiResourceNotFoundException(ApiResourceNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        ApiExceptionBody apiExceptionBody = new ApiExceptionBody(e.getMessage(),
                false, notFound, LocalDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiExceptionBody, notFound);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(value = {ApiRequestUnauthorizedException.class})
    public ResponseEntity<Object> handleApiRequestUnauthorizedException(ApiRequestUnauthorizedException e) {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;

        ApiExceptionBody apiExceptionBody = new ApiExceptionBody(e.getMessage(),
                false, unauthorized, LocalDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiExceptionBody, unauthorized);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(value = {ApiResourceTakenException.class})
    public ResponseEntity<Object> handleApiResourceTakenException(ApiResourceTakenException e) {
        HttpStatus resourceTaken = HttpStatus.CONFLICT;

        ApiExceptionBody apiExceptionBody = new ApiExceptionBody(e.getMessage(),
                false, resourceTaken, LocalDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiExceptionBody, resourceTaken);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        ApiMultiExceptionBody apiExceptionBody = new ApiMultiExceptionBody(result.values(),
                false, badRequest, LocalDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiExceptionBody, badRequest);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(value = {ApiInternalServerException.class})
    public ResponseEntity<Object> handleApiInternalServerException(ApiInternalServerException e) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiExceptionBody apiExceptionBody = new ApiExceptionBody(e.getMessage(),
                false, internalServerError, LocalDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiExceptionBody, internalServerError);
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<Object> handleRetryableException(RetryableException e) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiExceptionBody apiExceptionBody = new ApiExceptionBody(ServiceEnum.containsServiceName(e.getMessage()),
                false, internalServerError, LocalDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiExceptionBody, internalServerError);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ExceptionHandler(value = {ApiAccessForbiddenException.class})
    public ResponseEntity<Object> handleApiAccessForbiddenException(ApiAccessForbiddenException e) {
        HttpStatus forbidden = HttpStatus.FORBIDDEN;

        ApiExceptionBody apiExceptionBody = new ApiExceptionBody(e.getMessage(),
                false, forbidden, LocalDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiExceptionBody, forbidden);
    }

}
