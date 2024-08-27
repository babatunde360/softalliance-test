package com.babatunde.authentication_service.feign.employee;


import com.babatunde.authentication_service.feign.FeignConfig;
import com.babatunde.authentication_service.model.employee.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "employee-management", configuration = {FeignConfig.class})
public interface EmployeeManagementFeignProxy {

    @GetMapping("/feign/v1/employees/{id}")
    Employee getEmployeeById(@PathVariable Long id);

}
