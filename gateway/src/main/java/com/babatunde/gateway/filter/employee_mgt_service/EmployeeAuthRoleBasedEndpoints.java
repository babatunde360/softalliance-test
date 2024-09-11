package com.babatunde.gateway.filter.employee_mgt_service;


import com.babatunde.gateway.filter.authentication_service.Role;
import com.babatunde.gateway.model.Endpoint;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class EmployeeAuthRoleBasedEndpoints {

    private EmployeeAuthRoleBasedEndpoints() {
        throw new AssertionError("This class should not be instantiated.");
    }

    public static final String BASE_DEPARTMENT_PATH = "/api/v1/departments";
    public static final String BASE_EMPLOYEE_PATH = "/api/v1/employees";
    public static final String ID_REGEX = "/([^/]+)";
    static final List<Endpoint> roleEndpoints = new ArrayList<>(List.of(
            //Authenticated Air Cargo Controller
            new Endpoint(BASE_DEPARTMENT_PATH,
                    HttpMethod.GET,
                    Collections.singletonList(Role.ADMIN.toString())
            ),
            new Endpoint(BASE_DEPARTMENT_PATH,
                    HttpMethod.POST,
                    Collections.singletonList(Role.ADMIN.toString())
            ),
            new Endpoint(BASE_DEPARTMENT_PATH,
                    HttpMethod.PUT,
                    Collections.singletonList(Role.ADMIN.toString()),
                    BASE_DEPARTMENT_PATH + ID_REGEX
            ),
            new Endpoint(BASE_DEPARTMENT_PATH,
                    HttpMethod.DELETE,
                    Collections.singletonList(Role.ADMIN.toString()),
                    BASE_DEPARTMENT_PATH + ID_REGEX
            ),
            new Endpoint(BASE_EMPLOYEE_PATH,
                    HttpMethod.GET,
                    Arrays.asList(Role.ADMIN.toString(), Role.MANAGER.toString())
            ),
            new Endpoint(BASE_EMPLOYEE_PATH,
                    HttpMethod.POST,
                    Collections.singletonList(Role.ADMIN.toString())
            ),
            new Endpoint(BASE_EMPLOYEE_PATH,
                    HttpMethod.PUT,
                    Collections.singletonList(Role.ADMIN.toString()),
                    BASE_EMPLOYEE_PATH + ID_REGEX
            ),
            new Endpoint(BASE_EMPLOYEE_PATH,
                    HttpMethod.DELETE,
                    Collections.singletonList(Role.ADMIN.toString()),
                    BASE_EMPLOYEE_PATH + ID_REGEX
            ),
            new Endpoint(BASE_EMPLOYEE_PATH,
                    HttpMethod.GET,
                    Arrays.asList(Role.ADMIN.toString(), Role.MANAGER.toString(), Role.USER.toString()),
                    BASE_EMPLOYEE_PATH + ID_REGEX),
            new Endpoint(BASE_EMPLOYEE_PATH,
                    HttpMethod.GET,
                    Collections.singletonList(Role.ADMIN.toString()),
                    BASE_EMPLOYEE_PATH + "/department" + ID_REGEX)
    ));

}