package com.babatunde.gateway.config;

import com.babatunde.gateway.filter.authentication_service.AuthenticationServiceFilter;
import com.babatunde.gateway.filter.employee_mgt_service.EmployeeAuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    private static final String REPLACEMENT_PATH = "/${path}";

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder,
                                      AuthenticationServiceFilter authFilter,
                                      EmployeeAuthFilter employeeAuthFilter) {

        return builder.routes()
                .route("authentication-service", r -> r.path("/authentication-service/**")
                        .filters(f -> f.rewritePath("/authentication-service/(?<path>.*)", REPLACEMENT_PATH)
                                .filter(authFilter.apply(new AuthenticationServiceFilter.Config())))
                        .uri("lb://authentication-service"))

                .route("employee-management", r -> r.path("/employee-management/**")
                        .filters(f -> f.rewritePath("/employee-management/(?<path>.*)", REPLACEMENT_PATH)
                                .filter(employeeAuthFilter.apply(new EmployeeAuthFilter.Config()))
                        )
                        .uri("lb://employee-management"))
                .build();
    }

}