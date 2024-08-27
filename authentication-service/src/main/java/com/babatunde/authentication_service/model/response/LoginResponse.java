package com.babatunde.authentication_service.model.response;

import com.babatunde.authentication_service.model.user.UserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private UserDTO user;
    private Token token;
}
