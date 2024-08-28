package com.babatunde.employee.service.auth;

import com.babatunde.employee.model.auth.UserCreationJSON;
import com.babatunde.employee.model.auth.UserUpdateJSON;

public interface AuthService {

    boolean createUser(UserCreationJSON creationJSON);
    boolean updateUser(UserUpdateJSON updateJSON);
    boolean deleteUser(String email);


}
