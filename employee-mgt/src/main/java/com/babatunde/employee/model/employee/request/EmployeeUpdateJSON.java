package com.babatunde.employee.model.employee.request;

import com.babatunde.employee.model.auth.AuthRole;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateJSON {
    @Email
    private String email;
    private Long departmentId;
    private AuthRole role;
}
