package com.babatunde.employee.model.department.response;

import com.babatunde.employee.model.department.Department;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DepartmentDTOMapper implements Function<Department, DepartmentDTO> {
    @Override
    public DepartmentDTO apply(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }
}
