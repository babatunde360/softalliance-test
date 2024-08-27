package com.babatunde.authentication_service.service.employee;

import com.babatunde.authentication_service.feign.employee.EmployeeManagementFeignProxy;
import com.babatunde.authentication_service.model.employee.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeMgtServiceImpl implements EmployeeMgtService {

    private final EmployeeManagementFeignProxy employeeProxy;

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeProxy.getEmployeeById(id);
    }

}
