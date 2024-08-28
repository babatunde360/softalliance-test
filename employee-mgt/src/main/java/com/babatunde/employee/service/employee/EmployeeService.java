package com.babatunde.employee.service.employee;

import com.babatunde.employee.model.employee.Employee;
import com.babatunde.employee.model.employee.request.EmployeeCreationJSON;
import com.babatunde.employee.model.employee.request.EmployeeUpdateJSON;
import com.babatunde.employee.model.employee.response.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO create(EmployeeCreationJSON creationJSON);

    EmployeeDTO update(Long id, EmployeeUpdateJSON updateJSON);

    void delete(Long id);

    EmployeeDTO findById(Long id);

    List<EmployeeDTO> findAll();

    List<EmployeeDTO> findByDepartmentId(Long departmentId);

    Employee feignFindById(Long id);

    boolean findByEmail(String mail);
}
