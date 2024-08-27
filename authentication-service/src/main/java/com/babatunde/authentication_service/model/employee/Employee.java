package com.babatunde.authentication_service.model.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    long id;
    String email;
    String firstName;
    String lastName;
    String department;
    String phoneNumber;
}
