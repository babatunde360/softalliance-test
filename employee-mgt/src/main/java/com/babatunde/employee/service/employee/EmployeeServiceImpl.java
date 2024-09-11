package com.babatunde.employee.service.employee;

import com.babatunde.employee.exception.ApiBadRequestException;
import com.babatunde.employee.exception.ApiInternalServerException;
import com.babatunde.employee.exception.ApiResourceNotFoundException;
import com.babatunde.employee.exception.ApiResourceTakenException;
import com.babatunde.employee.model.auth.UserCreationJSON;
import com.babatunde.employee.model.auth.UserUpdateJSON;
import com.babatunde.employee.model.department.Department;
import com.babatunde.employee.model.employee.Employee;
import com.babatunde.employee.model.employee.request.EmployeeCreationJSON;
import com.babatunde.employee.model.employee.request.EmployeeUpdateJSON;
import com.babatunde.employee.model.employee.response.EmployeeDTO;
import com.babatunde.employee.model.employee.response.EmployeeDTOMapper;
import com.babatunde.employee.repository.EmployeeRepository;
import com.babatunde.employee.service.auth.AuthService;
import com.babatunde.employee.service.department.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    public static final String EMPLOYEE_NOT_FOUND = "Employee not found";
    private final EmployeeRepository repository;
    private final DepartmentService departmentService;
    private final AuthService authService;
    private final EmployeeDTOMapper mapper;


    @Override
    @Transactional
    public EmployeeDTO create(EmployeeCreationJSON creationJSON) {
        if (creationJSON.getFirstName() == null || creationJSON.getLastName() == null ||
                creationJSON.getEmail() == null || creationJSON.getRole() == null ||
                creationJSON.getPassword() == null || creationJSON.getDepartmentId() == null
        ) {
            throw new ApiBadRequestException("Incomplete data");
        }

        if (Boolean.TRUE.equals(repository.existsByEmail(creationJSON.getEmail()))) {
            throw new ApiResourceTakenException("Email already taken");
        }

        Employee.EmployeeBuilder employee = Employee.builder()
                .firstName(creationJSON.getFirstName())
                .lastName(creationJSON.getLastName())
                .email(creationJSON.getEmail());

        if (creationJSON.getDepartmentId() != null) {
            Department department = departmentService.internalFindByID(creationJSON.getDepartmentId());
            employee.department(department);
        }

        Employee savedEmployee = repository.save(employee.build());

        UserCreationJSON userCreationJSON = UserCreationJSON.builder()
                .username(creationJSON.getEmail())
                .employeeId(savedEmployee.getId())
                .role(creationJSON.getRole())
                .password(creationJSON.getPassword())
                .build();


        if (!authService.createUser(userCreationJSON)) {
            throw new ApiInternalServerException("User creation failed");
        }


        return mapper.apply(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO update(Long id, EmployeeUpdateJSON updateJSON) {

        if (updateJSON.getEmail() == null && updateJSON.getDepartmentId() == null) {
            throw new ApiBadRequestException("Email or Department ID must be provided");
        }
        UserUpdateJSON.UserUpdateJSONBuilder userUpdateJSON = UserUpdateJSON.builder();

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ApiResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        if (updateJSON.getEmail() != null) {
            employee.setEmail(updateJSON.getEmail());
            userUpdateJSON.username(employee.getEmail());
        }

        if (updateJSON.getDepartmentId() != null) {
            Department department = departmentService.internalFindByID(updateJSON.getDepartmentId());
            employee.setDepartment(department);
        }

        if (updateJSON.getRole() != null) {
            userUpdateJSON.role(updateJSON.getRole());
        }

        if (!authService.updateUser(userUpdateJSON.build())) {
            throw new ApiInternalServerException("User update failed");
        }

        Employee updatedEmployee = repository.save(employee);
        return mapper.apply(updatedEmployee);
    }

    @Override
    public void delete(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new ApiResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        if (!authService.deleteUser(employee.getEmail())) {
            throw new ApiInternalServerException("User deletion failed");
        }

        repository.delete(employee);
    }

    @Override
    public EmployeeDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ApiResourceNotFoundException(EMPLOYEE_NOT_FOUND));
    }

    @Override
    public List<EmployeeDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper)
                .toList();
    }

    @Override
    public List<EmployeeDTO> findByDepartmentId(Long departmentId) {
        return repository.findByDepartmentId(departmentId).stream()
                .map(mapper)
                .toList();
    }

    @Override
    public EmployeeDTO feignFindById(Long id) {
        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ApiResourceNotFoundException(EMPLOYEE_NOT_FOUND));
    }

    @Override
    public boolean findByEmail(String mail) {
        return repository.existsByEmail(mail);
    }
}
