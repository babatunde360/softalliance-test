package com.babatunde.authentication_service.service.jwt;

import com.babatunde.authentication_service.exception.ApiBadRequestException;
import com.babatunde.authentication_service.exception.ApiRequestUnauthorizedException;
import com.babatunde.authentication_service.model.response.TokenDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {


    public static final String INVALID_TOKEN = "Invalid token";
    @Value("${jwt.accessTokenSecretKey}")
    private String accessTokenSecret;

    @Value("${jwt.accessTokenValiditySeconds}")
    private long accessTokenTime;

    @Value("${jwt.refreshTokenSecretKey}")
    private String refreshTokenSecret;

    @Value("${jwt.refreshTokenValiditySeconds}")
    private long refreshTokenTime;


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        validateToken(token, secretKey);
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token, String secretKey) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("Token has expired", e);
            throw new ApiRequestUnauthorizedException("Token has expired", e);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.error(INVALID_TOKEN, e);
            throw new ApiBadRequestException(INVALID_TOKEN, e);
        }
    }

    private Key getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private void validateToken(String token, String secretKey) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey(secretKey)).build().parseClaimsJws(token);
        } catch (Exception e) {
            throw new ApiBadRequestException(INVALID_TOKEN);
        }
    }

    @Override
    public TokenDetail generateAccessToken(Map<String, Object> claims, String username) {
        String token = generateToken(claims, username, accessTokenTime, accessTokenSecret);
        return new TokenDetail(token, accessTokenTime);
    }

    @Override
    public TokenDetail generateRefreshToken(Map<String, Object> claims, String username) {
        String token = generateToken(claims, username, refreshTokenTime, refreshTokenSecret);
        return new TokenDetail(token, refreshTokenTime);
    }

    @Override
    public String extractUserNameFromRefreshToken(String token) {
        return extractClaim(token, Claims::getSubject, refreshTokenSecret);
    }

    @Override
    public boolean isTokenAboutToExpire(String refreshToken) {
        return extractExpiration(refreshToken, refreshTokenSecret).before(new Date(accessTokenTime + 1000 * 60 * 5));
    }


    private Date extractExpiration(String token, String secret) {
        return extractClaim(token, Claims::getExpiration, secret);
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            String username,
            long expirationTime,
            String secret
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 60 * 60))
                .signWith(getSignInKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }


}
