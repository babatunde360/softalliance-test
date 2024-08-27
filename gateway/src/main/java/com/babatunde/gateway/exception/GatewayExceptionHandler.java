package com.babatunde.gateway.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class GatewayExceptionHandler {

    @Bean
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return new CustomErrorWebExceptionHandler();
    }

    private static class CustomErrorWebExceptionHandler implements ErrorWebExceptionHandler {

        @Override
        public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
            ApiExceptionBody apiExceptionBody = new ApiExceptionBody();
            HttpStatus httpStatus;
            apiExceptionBody.setMessage(ex.getMessage());
            apiExceptionBody.setSuccess(false);
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            apiExceptionBody.setTimestamp(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            if (ex instanceof ApiRequestUnauthorizedException) {
                httpStatus = HttpStatus.UNAUTHORIZED;
                apiExceptionBody.setHttpStatus(httpStatus);
            } else if (ex instanceof ApiRequestForbiddenException) {
                httpStatus = HttpStatus.FORBIDDEN;
                apiExceptionBody.setHttpStatus(httpStatus);
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            apiExceptionBody.setSuccess(false);

            exchange.getResponse().setStatusCode(httpStatus);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            try {
                return exchange.getResponse().writeWith(
                        Mono.just(exchange.getResponse()
                                .bufferFactory().wrap(new ObjectMapper().writeValueAsBytes(apiExceptionBody)))
                );
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
