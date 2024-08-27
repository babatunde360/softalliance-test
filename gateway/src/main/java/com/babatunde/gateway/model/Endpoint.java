package com.babatunde.gateway.model;

import org.springframework.http.HttpMethod;

import java.util.List;

public record Endpoint(String path, HttpMethod method, List<String> roles, String regex) {
    public Endpoint(String path, HttpMethod method, List<String> roles) {
        this(path, method, roles, null);
    }
}