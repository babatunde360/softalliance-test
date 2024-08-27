package com.babatunde.authentication_service.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private TokenDetail accessToken;
    private TokenDetail refreshToken;
}
