package com.babatunde.employee.service.feign.auth;


import com.babatunde.employee.model.auth.UserCreationJSON;
import com.babatunde.employee.model.auth.UserUpdateJSON;
import com.babatunde.employee.service.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "authentication-service", configuration = {FeignConfig.class})
public interface AuthFeignProxy {

    static final String BASE_URL = "/v1/feign/user";

    @PostMapping(BASE_URL)
    boolean createUser(@RequestBody UserCreationJSON creationJSON);

    @PutMapping(BASE_URL)
    boolean updateUser(@RequestBody UserUpdateJSON updateJSON);

    @DeleteMapping(BASE_URL + "/{email}")
    boolean deleteUser(@PathVariable String email);

}
