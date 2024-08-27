package com.babatunde.authentication_service.service.token;

import com.babatunde.authentication_service.model.response.Token;

public interface TokenService {
    Token generateToken(String username, long id, String role, boolean enabled);
    String getUsernameFromToken(String token);
    Token refreshToken(String refreshToken, String username, long id, String role, boolean enabled);
//    boolean validateToken(String token);
}
