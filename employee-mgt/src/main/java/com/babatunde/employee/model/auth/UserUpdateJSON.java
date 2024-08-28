package com.babatunde.employee.model.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateJSON {
    private String username;
    private String password;
    private AuthRole role;
    private Boolean enabled;
}
