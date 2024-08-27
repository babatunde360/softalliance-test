package com.babatunde.authentication_service.service.token;

import com.babatunde.authentication_service.model.response.Token;
import com.babatunde.authentication_service.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;


    @Override
    public Token generateToken(String username, long id, String role, boolean enabled) {
        Token.TokenBuilder tokenBuilder = Token.builder();

        Map<String, Object> claims = Map.of(
                "userId", id,
                "role", role,
                "enabled", enabled
        );

        tokenBuilder.accessToken(jwtService.generateAccessToken(claims, username));
        tokenBuilder.refreshToken(jwtService.generateRefreshToken(claims, username));

        return tokenBuilder.build();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtService.extractUserNameFromRefreshToken(token);
    }

    @Override
    public Token refreshToken(String refreshToken, String username, long id, String role, boolean enabled) {
        Token.TokenBuilder tokenBuilder = Token.builder();

        Map<String, Object> claims = Map.of(
                "userId", id,
                "role", role,
                "enabled", enabled
        );


        if (jwtService.isTokenAboutToExpire(refreshToken)) {
            tokenBuilder.refreshToken(jwtService.generateRefreshToken(claims, username));
        }

        tokenBuilder.accessToken(jwtService.generateAccessToken(claims, username));

        return tokenBuilder.build();
    }

//    @Override
//    public boolean validateToken(String token) {
//        return jwtService.validateAccessToken(token);
//    }
}
