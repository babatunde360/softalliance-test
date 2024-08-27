package com.babatunde.authentication_service.service.user;


import com.babatunde.authentication_service.exception.ApiAccessForbiddenException;
import com.babatunde.authentication_service.exception.ApiBadRequestException;
import com.babatunde.authentication_service.exception.ApiResourceNotFoundException;
import com.babatunde.authentication_service.exception.ApiResourceTakenException;
import com.babatunde.authentication_service.model.employee.Employee;
import com.babatunde.authentication_service.model.request.user.UserCreationJSON;
import com.babatunde.authentication_service.model.request.user.UserUpdateJSON;
import com.babatunde.authentication_service.model.response.LoginResponse;
import com.babatunde.authentication_service.model.response.Token;
import com.babatunde.authentication_service.model.response.TokenDetail;
import com.babatunde.authentication_service.model.user.Role;
import com.babatunde.authentication_service.model.user.User;
import com.babatunde.authentication_service.model.user.UserDTO;
import com.babatunde.authentication_service.repository.user.UserRepository;
import com.babatunde.authentication_service.service.employee.EmployeeMgtService;
import com.babatunde.authentication_service.service.jwt.JwtService;
import com.babatunde.authentication_service.service.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND = "User not found";
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final EmployeeMgtService employeeMgtService;


    @Override
    public User createUser(UserCreationJSON creationJSON) {
        if (userRepository.existsByUsername(creationJSON.getUsername())) {
            log.error("User with email {} already exists", creationJSON.getUsername());
            throw new ApiResourceTakenException("User with email already exists");
        }

        User user = User.builder()
                .username(creationJSON.getUsername())
                .password(passwordEncoder.encode(creationJSON.getPassword()))
                .employeeId(creationJSON.getEmployeeId())
                .enabled(true)
                .role(creationJSON.getRole())
                .build();

        return userRepository.save(user);
    }

    @Override
    public LoginResponse login(String email, String password) {

        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new ApiResourceNotFoundException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ApiBadRequestException("Incorrect password please try again");
        }

        return getUserDTO(user);


    }

    private LoginResponse getUserDTO(User user) {


        if (!user.isEnabled()) {
            throw new ApiAccessForbiddenException("Your account has been blocked, please contact the admin");
        }

        Employee employee = employeeMgtService.getEmployeeById(user.getEmployeeId());


        Token token = tokenService.generateToken(user.getUsername(), user.getId(),
                user.getRole().toString(), user.isEnabled());

        UserDTO userDTO = userMapper(user, employee);

        return LoginResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }


    private UserDTO userMapper(User user, Employee employee) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getUsername())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .role(user.getRole().toString())
                .department(employee.getDepartment())
                .phoneNumber(employee.getPhoneNumber())
                .build();
    }

    @Override
    @Transactional
    public User updateUser(UserUpdateJSON updateJSON) {
        User user = userRepository.findByUsername(updateJSON.getUsername())
                .orElseThrow(() -> new ApiResourceNotFoundException(USER_NOT_FOUND));

        if (updateJSON.getUsername() == null && updateJSON.getPassword() == null &&
                updateJSON.getRole() == null && updateJSON.getEnabled() == null) {
            throw new ApiBadRequestException("No data to update");
        }

        if (updateJSON.getUsername() != null) {
            user.setUsername(updateJSON.getUsername());
        }

        if (updateJSON.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateJSON.getPassword()));
        }

        if (updateJSON.getRole() != null) {
            user.setRole(updateJSON.getRole());
        }

        if (updateJSON.getEnabled() != null) {
            user.setEnabled(updateJSON.getEnabled());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean deleteUser(String email) {
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new ApiResourceNotFoundException(USER_NOT_FOUND));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new ApiAccessForbiddenException("Admin account cannot be deleted");
        }
        userRepository.delete(user);
        return true;
    }

    @Override
    public Token refreshToken(String refreshToken) {
        String username = tokenService.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiResourceNotFoundException(USER_NOT_FOUND));

        return tokenService.refreshToken(refreshToken, user.getUsername(), user.getId(),
                user.getRole().toString(), user.isEnabled());
    }
}
