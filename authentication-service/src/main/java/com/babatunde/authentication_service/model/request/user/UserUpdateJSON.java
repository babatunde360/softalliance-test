package com.babatunde.authentication_service.model.request.user;

import com.babatunde.authentication_service.model.user.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateJSON {
    private String username;
    private String password;
    private Role role;
    private Boolean enabled;
}
