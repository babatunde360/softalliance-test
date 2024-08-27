package com.babatunde.authentication_service.model.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    long id;
    String email;
    String firstName;
    String lastName;
    String role;
    String department;
    String phoneNumber;
}
