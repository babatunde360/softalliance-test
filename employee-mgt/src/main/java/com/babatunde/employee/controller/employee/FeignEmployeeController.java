package com.babatunde.employee.controller.employee;

import com.babatunde.employee.model.employee.Employee;
import com.babatunde.employee.service.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Hidden
@RequestMapping("/feign/v1/employees")
public class FeignEmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public Employee getEmployeeByEmail(@PathVariable Long id) {
        return employeeService.feignFindById(id);
    }

}
