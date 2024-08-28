package com.babatunde.employee.service.auth;

import com.babatunde.employee.model.auth.UserCreationJSON;
import com.babatunde.employee.model.auth.UserUpdateJSON;
import com.babatunde.employee.service.feign.auth.AuthFeignProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthFeignProxy authFeignProxy;


    @Override
    public boolean createUser(UserCreationJSON creationJSON) {
        return authFeignProxy.createUser(creationJSON);
    }

    @Override
    public boolean updateUser(UserUpdateJSON updateJSON) {
        return authFeignProxy.updateUser(updateJSON);
    }

    @Override
    public boolean deleteUser(String email) {
        return authFeignProxy.deleteUser(email);
    }
}
