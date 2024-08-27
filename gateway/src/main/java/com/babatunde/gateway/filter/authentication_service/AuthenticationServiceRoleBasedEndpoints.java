package com.babatunde.gateway.filter.authentication_service;


import com.babatunde.gateway.model.Endpoint;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationServiceRoleBasedEndpoints {

    private AuthenticationServiceRoleBasedEndpoints() {
        throw new AssertionError("This class should not be instantiated.");
    }

    static final List<Endpoint> roleEndpoints = new ArrayList<>(List.of(
//            new Endpoint("/v1/warehouse/users",
//                    HttpMethod.POST, Arrays.asList(AdminRole.SUPER_ADMIN.toString(), AdminRole.ADMIN.toString()))
    ));

}