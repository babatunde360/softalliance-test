package com.babatunde.employee.model.department.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreationJSON {
    @NotBlank(message = "Department name is required")
    private String name;
}
