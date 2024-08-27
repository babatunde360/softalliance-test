package com.babatunde.gateway.filter.authentication_service;

import com.babatunde.gateway.exception.ApiRequestForbiddenException;
import com.babatunde.gateway.exception.ApiRequestUnauthorizedException;
import com.babatunde.gateway.utils.JwtUtil;
import com.babatunde.gateway.utils.RouteValidator;
import com.babatunde.gateway.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationServiceFilter extends AbstractGatewayFilterFactory<AuthenticationServiceFilter.Config> {


    private final JwtUtil jwtUtil;
    private final Utility utility;
    private final RouteValidator validator;


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();
            HttpMethod method = exchange.getRequest().getMethod();
            ServerHttpRequest request = null;

            if (path.contains("feign")) {
                throw new ApiRequestUnauthorizedException("Please don't try to access the feign endpoints again");
            }

            if (validator.isSecured.test(exchange.getRequest())
                    && !path.contains("/swagger-ui/") && !path.contains("/auth")) {

                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ApiRequestUnauthorizedException("Missing authorization header");
                }

                String authHeader = utility.validateAndExtractToken(exchange);

                // Get the role from the token
                String role = jwtUtil.extractClaimFromToken("role", authHeader);
                String userId = jwtUtil.extractClaimFromToken("userId", authHeader);


                // Get the endpoints for the role
                if (utility.hasRightPermission(path, method, role, AuthenticationServiceRoleBasedEndpoints.roleEndpoints)) {
                    if (method.equals(HttpMethod.GET)) {
                        throw new ApiRequestForbiddenException("You do not have the required access to the requested resource");
                    } else {
                        throw new ApiRequestForbiddenException("You do not have the required permission to perform this action");
                    }
                }

                log.info("Endpoint with path: {}", path);

                request = exchange.getRequest().mutate()
                        .header("client-key", userId)
                        .build();
            }
            assert request != null;
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {

    }
}

