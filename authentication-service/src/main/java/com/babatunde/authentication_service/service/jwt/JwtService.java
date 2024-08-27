package com.babatunde.authentication_service.service.jwt;


import com.babatunde.authentication_service.model.response.TokenDetail;

import java.util.Map;

public interface JwtService {

    TokenDetail generateAccessToken(Map<String, Object> claims, String username);

    TokenDetail generateRefreshToken(Map<String, Object> claims, String username);


    String extractUserNameFromRefreshToken(String token);

    boolean isTokenAboutToExpire(String refreshToken);
}
