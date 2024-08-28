package com.babatunde.employee.model.employee.request;

import com.babatunde.employee.model.auth.AuthRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreationJSON {
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @Email
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "password is required")
    private String password;
    @NotNull(message = "Department id is required")
    private Long departmentId;
    @NotNull(message = "Role is required")
    private AuthRole role;
}
