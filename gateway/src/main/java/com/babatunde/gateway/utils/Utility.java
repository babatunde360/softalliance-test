package com.babatunde.gateway.utils;

import com.babatunde.gateway.exception.ApiRequestUnauthorizedException;
import com.babatunde.gateway.model.Endpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class Utility {

    private final JwtUtil jwtUtil;

    public boolean hasRightPermission(String path, HttpMethod method, String role, List<Endpoint> roleEndpoints) {
        Optional<Endpoint> filteredEndpoint = roleEndpoints.stream()
                .filter(endpoint -> {
                    if (endpoint.regex() != null) {
                        // if regex is present, use it to match the path
                        Pattern pattern = Pattern.compile(endpoint.regex());
                        return pattern.matcher(path).matches();
                    } else {
                        // if regex is not present, check for equality
                        return endpoint.path().equals(path);
                    }
                })
                .filter(endpoint -> endpoint.method().equals(method))
                .findFirst();

        return filteredEndpoint.map(endpoint -> !endpoint.roles().contains(role)).orElse(false);
    }

    public String validateAndExtractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        log.info("Auth header: {}", authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
        }
        try {
            jwtUtil.validateToken(authHeader);
        } catch (Exception e) {
            log.error("The JWT error {}", e.getLocalizedMessage());
            throw new ApiRequestUnauthorizedException("Invalid Token...!");
        }
        return authHeader;
    }


}
