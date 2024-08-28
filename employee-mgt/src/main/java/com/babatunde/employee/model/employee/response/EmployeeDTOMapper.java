package com.babatunde.employee.model.employee.response;

import com.babatunde.employee.model.employee.Employee;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {
    @Override
    public EmployeeDTO apply(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .departmentName(employee.getDepartment() != null ?
                        employee.getDepartment().getName() : null)
                .build();
    }
}
