package com.babatunde.authentication_service.model.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotNull(message = "Refresh token is required")
    private String refreshToken;
}