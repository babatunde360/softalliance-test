package com.babatunde.authentication_service.service.employee;

import com.babatunde.authentication_service.model.employee.Employee;

public interface EmployeeMgtService {
    Employee getEmployeeById(Long id);
}
