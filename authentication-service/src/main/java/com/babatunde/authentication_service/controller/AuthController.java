package com.babatunde.authentication_service.controller;


import com.babatunde.authentication_service.model.request.auth.LoginRequest;
import com.babatunde.authentication_service.model.response.LoginResponse;
import com.babatunde.authentication_service.model.response.ResponseBody;
import com.babatunde.authentication_service.model.response.Token;
import com.babatunde.authentication_service.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Auth Controller")
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;



    @PostMapping("/login")
    @Operation(summary = "Login a user")
    public ResponseEntity<ResponseBody<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new ResponseBody<>(true,
                "User logged in successfully",
                userService.login(loginRequest.getEmail(), loginRequest.getPassword())));
    }

    @PostMapping("/token/refresh")
    @Operation(summary = "Refresh token")
    public ResponseEntity<ResponseBody<Token>> refreshToken(@RequestBody String refreshToken) {
        return ResponseEntity.ok(new ResponseBody<>(true,
                "Token refreshed successfully",
                userService.refreshToken(refreshToken)));
    }

}
