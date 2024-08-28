package com.babatunde.employee.service.feign;

import lombok.Getter;

@Getter
public enum ServiceEnum {
    AUTHENTICATION_SERVICE("authentication-service");

    private String serviceName;

    ServiceEnum(String serviceName) {
        this.serviceName = serviceName;
    }

    public static String containsServiceName(String input) {
        for (ServiceEnum service : ServiceEnum.values()) {
            if (input.contains(service.getServiceName())) {
                return service.serviceName + " is currently down. Please try again later.";
            }
        }
        return "Service is currently down or does not exist yet. Please try again later.";
    }
}
