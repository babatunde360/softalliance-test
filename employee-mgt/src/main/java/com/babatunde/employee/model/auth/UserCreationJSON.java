package com.babatunde.employee.model.auth;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationJSON {
    private String username;
    private String password;
    private Long employeeId;
    private AuthRole role;
}
