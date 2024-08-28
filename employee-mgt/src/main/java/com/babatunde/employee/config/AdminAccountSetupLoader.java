package com.babatunde.employee.config;


import com.babatunde.employee.model.auth.AuthRole;
import com.babatunde.employee.model.department.request.DepartmentCreationJSON;
import com.babatunde.employee.model.department.response.DepartmentDTO;
import com.babatunde.employee.model.employee.request.EmployeeCreationJSON;
import com.babatunde.employee.service.department.DepartmentService;
import com.babatunde.employee.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAccountSetupLoader implements CommandLineRunner {

    private final EmployeeService userService;
    private final DepartmentService departmentService;

    @Override
    public void run(String... args) throws Exception {

        try {
            if (!userService.findByEmail("admin@company.com")) {

                DepartmentCreationJSON.DepartmentCreationJSONBuilder department =
                        DepartmentCreationJSON.builder()
                                .name("Admin");

                DepartmentDTO createdDepartment = departmentService.create(department.build());

                EmployeeCreationJSON.EmployeeCreationJSONBuilder admin = EmployeeCreationJSON.builder();

                admin.firstName("Admin");
                admin.lastName("User");
                admin.email("admin@company.com");
                admin.password("Testing@123");
                admin.departmentId(createdDepartment.getId());
                admin.role(AuthRole.ADMIN);

                userService.create(admin.build());
            }
        } catch (Exception e) {
            log.info("An error occurred while setting up admin account: {}", e.getMessage());
        }
    }
}
