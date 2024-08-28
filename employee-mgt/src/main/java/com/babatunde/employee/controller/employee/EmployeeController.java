package com.babatunde.employee.controller.employee;

import com.babatunde.employee.model.employee.request.EmployeeCreationJSON;
import com.babatunde.employee.model.employee.request.EmployeeUpdateJSON;
import com.babatunde.employee.model.employee.response.EmployeeDTO;
import com.babatunde.employee.model.response.ResponseBody;
import com.babatunde.employee.service.employee.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Employee Management Controller")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create a new employee")
    public ResponseEntity<ResponseBody<EmployeeDTO>> createEmployee(@RequestBody @Valid EmployeeCreationJSON creationJSON,
                                                                    HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseBody<>(true,
                "Employee created successfully", employeeService.create(creationJSON)));

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an employee")
    public ResponseEntity<ResponseBody<EmployeeDTO>> updateEmployee(@PathVariable Long id, @RequestBody EmployeeUpdateJSON updateJSON,
                                                                    HttpServletRequest request) {

        EmployeeDTO updatedEmployee = employeeService.update(id, updateJSON);
        return ResponseEntity.ok(new ResponseBody<>(true, "Employee updated successfully",
                updatedEmployee));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee")
    public ResponseEntity<ResponseBody<Object>> deleteEmployee(@PathVariable Long id,
                                                               HttpServletRequest request) {

        employeeService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseBody<>(true, "Employee deleted successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all employees")
    public ResponseEntity<ResponseBody<List<EmployeeDTO>>> getAllEmployees(HttpServletRequest request) {
        return ResponseEntity.ok(new ResponseBody<>(true,
                "Employees retrieved successfully", employeeService.findAll()));
    }

    @GetMapping("/department/{id}")
    @Operation(summary = "Get employees by department")
    public ResponseEntity<ResponseBody<List<EmployeeDTO>>> getEmployeesByDepartment(@PathVariable Long id,
                                                                                    HttpServletRequest request) {

        return ResponseEntity.ok(new ResponseBody<>(true,
                "Employees retrieved successfully", employeeService.findByDepartmentId(id)));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get an employee")
    public ResponseEntity<ResponseBody<EmployeeDTO>> getEmployee(@PathVariable Long id,
                                                                 HttpServletRequest request) {

        return ResponseEntity.ok(new ResponseBody<>(true,
                "Employee retrieved successfully", employeeService.findById(id)));

    }

}
