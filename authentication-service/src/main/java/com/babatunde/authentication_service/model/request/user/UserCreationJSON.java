package com.babatunde.authentication_service.model.request.user;

import com.babatunde.authentication_service.model.user.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationJSON {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Password cannot be null")
    private String password;
    @NotNull(message = "Employee ID cannot be null")
    private Long employeeId;
    @NotNull(message = "Role cannot be null")
    private Role role;
}
