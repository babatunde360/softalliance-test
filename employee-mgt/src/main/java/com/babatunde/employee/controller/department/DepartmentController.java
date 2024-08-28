package com.babatunde.employee.controller.department;

import com.babatunde.employee.model.department.request.DepartmentCreationJSON;
import com.babatunde.employee.model.department.request.DepartmentUpdateJSON;
import com.babatunde.employee.model.department.response.DepartmentDTO;
import com.babatunde.employee.model.response.ResponseBody;
import com.babatunde.employee.service.department.DepartmentService;
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
@Tag(name = "Department Management Controller")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    //    POST /departments - Add a new department (admin only). repository save. Service create
    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<ResponseBody<DepartmentDTO>> createDepartment(@RequestBody @Valid DepartmentCreationJSON creationJSON,
                                                                        HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseBody<>(true,
                "Department created successfully", departmentService.create(creationJSON)));

    }


    //    GET /departments - View all departments (admin only). repository findAll service findAll
    @GetMapping
    @Operation(summary = "Get all departments")
    public ResponseEntity<ResponseBody<List<DepartmentDTO>>> getAllDepartments(HttpServletRequest request) {

        return ResponseEntity.ok(new ResponseBody<>(true, "Departments retrieved successfully",
                departmentService.findAll()));
    }


    //    PUT /departments/{id} - Update department details (admin only). repository update service update
    @PutMapping("/{id}")
    @Operation(summary = "Update a department")
    public ResponseEntity<ResponseBody<DepartmentDTO>> updateDepartment(@PathVariable Long id, @RequestBody DepartmentUpdateJSON updateJSON,
                                                                        HttpServletRequest request) {

        DepartmentDTO updatedDepartment = departmentService.update(id, updateJSON);
        return ResponseEntity.ok(new ResponseBody<>(true, "Department updated successfully",
                updatedDepartment));
    }

    //    DELETE /departments/{id} - Delete a department (admin only). repository delete service delete
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a department")
    public ResponseEntity<ResponseBody<Object>> deleteDepartment(@PathVariable Long id,
                                                                 HttpServletRequest request) {

        departmentService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseBody<>(true, "Department deleted successfully"));
    }


}
