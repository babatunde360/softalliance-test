package com.babatunde.authentication_service.controller;

import com.babatunde.authentication_service.model.request.user.UserCreationJSON;
import com.babatunde.authentication_service.model.request.user.UserUpdateJSON;
import com.babatunde.authentication_service.model.user.User;
import com.babatunde.authentication_service.service.user.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Hidden
@RequestMapping("/v1/feign/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public boolean createUser(@RequestBody UserCreationJSON creationJSON) {
        User user = userService.createUser(creationJSON);
        return user != null;
    }


    @PutMapping
    public boolean updateUser(@RequestBody UserUpdateJSON updateJSON) {
        User user = userService.updateUser(updateJSON);
        return user != null;
    }


    @DeleteMapping("/{email}")
    public boolean deleteUser(@PathVariable String email) {
        return userService.deleteUser(email);
    }

}
