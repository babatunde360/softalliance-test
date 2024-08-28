package com.babatunde.employee.service.employee;

import com.babatunde.employee.exception.ApiResourceNotFoundException;
import com.babatunde.employee.model.department.Department;
import com.babatunde.employee.model.employee.Employee;
import com.babatunde.employee.model.employee.request.EmployeeCreationJSON;
import com.babatunde.employee.model.employee.request.EmployeeUpdateJSON;
import com.babatunde.employee.model.employee.response.EmployeeDTO;
import com.babatunde.employee.model.employee.response.EmployeeDTOMapper;
import com.babatunde.employee.repository.EmployeeRepository;
import com.babatunde.employee.service.auth.AuthService;
import com.babatunde.employee.service.department.DepartmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    EmployeeDTOMapper mapper;

    @Mock
    DepartmentService departmentService;

    @Mock
    AuthService authService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department itDepartment;
    private Department financeDepartment;
    private Employee employee1;
    private Employee employee2;
    private EmployeeDTO employeeDTO1;
    private EmployeeDTO employeeDTO2;
    private List<Employee> employees;
    private List<EmployeeDTO> employeeDTOs;

    @BeforeEach
    void setUp() {
        itDepartment = Department.builder()
                .id(1L)
                .name("IT").build();

        financeDepartment = Department.builder()
                .id(2L)
                .name("Finance").build();

        employee1 = Employee.builder().firstName("Adewale")
                .lastName("Apenku").department(itDepartment).build();
        employee2 = Employee.builder().firstName("Adewale")
                .lastName("Apenku").department(financeDepartment).build();
        employeeDTO1 = EmployeeDTO.builder().firstName("Adewale")
                .lastName("Apenku").departmentName("IT").build();
        employeeDTO2 = EmployeeDTO.builder().firstName("Adewale")
                .lastName("Apenku").departmentName("Finance").build();

        employees = List.of(employee1, employee2);
        employeeDTOs = List.of(employeeDTO1, employeeDTO2);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(employeeRepository, mapper, departmentService);
    }

    @Test
    void shouldCreateEmployee_WhenValidRequest() {
        EmployeeCreationJSON creationJSON = EmployeeCreationJSON.builder()
                .email("apenku@gmail.com")
                .departmentId(1L)
                .firstName("Adewale")
                .lastName("Apenku")
                .build();

        when(departmentService.internalFindByID(creationJSON.getDepartmentId())).thenReturn(itDepartment);
        when(authService.createUser(Mockito.any())).thenReturn(true);
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee1);
        when(mapper.apply(employee1)).thenReturn(employeeDTO1);

        EmployeeDTO result = employeeService.create(creationJSON);

        assertThat(result).isEqualTo(employeeDTO1);
    }

    @Test
    void shouldUpdateEmployee_WhenValidRequest() {
        Long employeeId = 1L;
        EmployeeUpdateJSON updateJSON = EmployeeUpdateJSON.builder()
                .departmentId(2L)
                .build();

        Employee updatedEmployee = Employee.builder().id(employeeId).firstName("Adewale").lastName("Apenku").department(financeDepartment).build();
        EmployeeDTO updatedEmployeeDTO = EmployeeDTO.builder().firstName("Adewale").lastName("Apenku").departmentName("Finance").build();

        when(departmentService.internalFindByID(updateJSON.getDepartmentId())).thenReturn(financeDepartment);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee1));
        when(authService.updateUser(Mockito.any())).thenReturn(true);
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(updatedEmployee);
        when(mapper.apply(updatedEmployee)).thenReturn(updatedEmployeeDTO);
        EmployeeDTO result = employeeService.update(employeeId, updateJSON);

        assertThat(result).isEqualTo(updatedEmployeeDTO);
    }

    @Test
    void shouldDeleteEmployee_WhenEmployeeExists() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee1));
        when(authService.deleteUser(Mockito.any())).thenReturn(true);
        employeeService.delete(employeeId);

        verify(employeeRepository).delete(employee1);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenEmployeeDoesNotExist() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.delete(employeeId))
                .isInstanceOf(ApiResourceNotFoundException.class)
                .hasMessage("Employee not found");
    }

    @Test
    void shouldReturnEmployeeDTO_WhenEmployeeExists() {
        Long employeeId = 1L;

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee1));
        when(mapper.apply(employee1)).thenReturn(employeeDTO1);

        EmployeeDTO result = employeeService.findById(employeeId);

        assertThat(result).isEqualTo(employeeDTO1);
    }

    @Test
    void shouldReturnListOfEmployeeDTOs_WhenEmployeesExist() {
        when(employeeRepository.findAll()).thenReturn(employees);
        when(mapper.apply(employee1)).thenReturn(employeeDTO1);
        when(mapper.apply(employee2)).thenReturn(employeeDTO2);

        List<EmployeeDTO> result = employeeService.findAll();

        assertThat(result).isEqualTo(employeeDTOs);
    }

    @Test
    void shouldReturnListOfEmployeeDTOs_WhenEmployeesExistInDepartment() {
        Long departmentId = 1L;
        List<Employee> itEmployees = List.of(employee1, employee1);
        List<EmployeeDTO> itEmployeeDTOs = List.of(employeeDTO1, employeeDTO1);

        when(employeeRepository.findByDepartmentId(departmentId)).thenReturn(itEmployees);
        when(mapper.apply(employee1)).thenReturn(employeeDTO1);

        List<EmployeeDTO> result = employeeService.findByDepartmentId(departmentId);

        assertThat(result).isEqualTo(itEmployeeDTOs);
    }


}

