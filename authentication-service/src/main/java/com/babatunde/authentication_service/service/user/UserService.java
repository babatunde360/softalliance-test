package com.babatunde.authentication_service.service.user;

import com.babatunde.authentication_service.model.request.user.UserCreationJSON;
import com.babatunde.authentication_service.model.request.user.UserUpdateJSON;
import com.babatunde.authentication_service.model.response.LoginResponse;
import com.babatunde.authentication_service.model.response.Token;
import com.babatunde.authentication_service.model.user.User;

public interface UserService {
    User createUser(UserCreationJSON user);

    LoginResponse login(String email, String password);

    User updateUser(UserUpdateJSON updateJSON);

    boolean deleteUser(String email);

    Token refreshToken(String refreshToken);
}
