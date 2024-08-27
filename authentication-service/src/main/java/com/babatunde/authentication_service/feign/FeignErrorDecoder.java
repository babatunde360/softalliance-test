package com.babatunde.authentication_service.feign;


import com.babatunde.authentication_service.exception.ApiBadRequestException;
import com.babatunde.authentication_service.exception.ApiInternalServerException;
import com.babatunde.authentication_service.exception.ApiResourceNotFoundException;
import com.babatunde.authentication_service.exception.ApiResourceTakenException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage;
        try {
            String responseBody = response.body() != null ? Util.toString(response.body().asReader(StandardCharsets.UTF_8)) : "";
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {
            });
            errorMessage = responseMap.get("message") != null ? responseMap.get("message").toString() : "No message found";
        } catch (IOException e) {
            errorMessage = "Failed to read response body";
        }

        return switch (HttpStatus.valueOf(response.status())) {
            case BAD_REQUEST -> new ApiBadRequestException(errorMessage);
            case NOT_FOUND -> new ApiResourceNotFoundException(errorMessage);
            case CONFLICT -> new ApiResourceTakenException(errorMessage);
            case INTERNAL_SERVER_ERROR -> new ApiInternalServerException(errorMessage);
            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }
}