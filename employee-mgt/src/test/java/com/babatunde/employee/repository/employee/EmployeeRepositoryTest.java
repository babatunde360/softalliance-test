package com.babatunde.employee.repository.employee;

import com.babatunde.employee.model.department.Department;
import com.babatunde.employee.model.employee.Employee;
import com.babatunde.employee.repository.EmployeeRepository;
import com.babatunde.employee.repository.department.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DataJpaTest(excludeAutoConfiguration = FlywayAutoConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @Test
    void shouldSaveAndReturnEmployee_WhenEmployeeIsSaved() {

        //Arrange

        Department department = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(department);

        Employee employee = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(department)
                .build();


        //Act
        Employee savedEmployee = employeeRepository.save(employee);
        //Assert

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }


    @Test
    void shouldUpdateEmployee_WhenEmployeeIsUpdated() {

        //Arrange

        Department department1 = Department.builder()
                .name("IT")
                .build();

        Department department2 = Department.builder()
                .name("HR")
                .build();

        departmentRepository.save(department1);

        Employee employee = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(department1)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        //Act
        savedEmployee.setDepartment(department2);

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //Assert
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getDepartment().getId()).isEqualTo(department2.getId());
    }

    @Test
    void shouldReturnEmployee_WhenEmployeeIsFoundById() {

        //Arrange

        Department department = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(department);

        Employee employee = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(department)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        //Act
        Employee foundEmployee = employeeRepository.findById(savedEmployee.getId()).orElse(null);

        //Assert
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getId()).isEqualTo(savedEmployee.getId());

    }

    @Test
    void shouldReturnNull_WhenEmployeeIsNotFoundById() {

        //Arrange & Act
        Employee employee = employeeRepository.findById(1L).orElse(null);

        //Assert
        assertThat(employee).isNull();

    }


    @Test
    void shouldReturnEmployee_WhenEmployeeIsFoundByEmail() {

        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(department);

        Employee employee = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(department)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        //Act
        Employee foundEmployee = employeeRepository.findByEmail(savedEmployee.getEmail()).orElse(null);

        //Assert
        assertThat(foundEmployee).isNotNull().isEqualTo(savedEmployee);

    }

    @Test
    void shouldReturnNull_WhenEmployeeIsNotFoundByEmail() {

        //Arrange & Act
        Employee employee = employeeRepository.findByEmail("apenku4life@gmail.com").orElse(null);

        //Assert
        assertThat(employee).isNull();
    }


    @Test
    void shouldReturnNull_WhenEmployeeIsDeleted() {

        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(department);

        Employee employee = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(department)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        //Act
        employeeRepository.delete(savedEmployee);

        //Assert
        Employee employeeById = employeeRepository.findById(savedEmployee.getId()).orElse(null);
        assertThat(employeeById).isNull();

    }

    @Test
    void shouldThrowException_WhenEmployeeIsSavedWithNullDepartment() {

        //Arrange

        Employee employee = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .build();

        //Assert & Act
        assertThatThrownBy(() -> employeeRepository.save(employee))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldReturnMoreThanOneEmployee_WhenEmployeesAreFound() {

        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(department);

        Department department2 = Department.builder()
                .name("HR")
                .build();

        departmentRepository.save(department2);


        Employee employee1 = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(department)
                .build();

        employeeRepository.save(employee1);


        Employee employee2 = Employee.builder()
                .firstName("Kayode")
                .lastName("Apenku")
                .email("kay4show@gmail.com")
                .department(department)
                .build();

        employeeRepository.save(employee2);

        Employee employee3 = Employee.builder()
                .firstName("Bayonle")
                .lastName("Apenku")
                .email("bayus@aol.com")
                .department(department2)
                .build();

        employeeRepository.save(employee3);

        //Act
        List<Employee> employees = employeeRepository.findAll();

        //Assert
        assertThat(employees).isNotNull()
                .hasSize(3);
    }


    @Test
    void shouldReturnListContainingEmployees_WhenEmployeesAreFound() {

        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(department);

        Department department2 = Department.builder()
                .name("HR")
                .build();

        departmentRepository.save(department2);

        Employee employee1 = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(department)
                .build();

        employeeRepository.save(employee1);


        Employee employee2 = Employee.builder()
                .firstName("Kayode")
                .lastName("Apenku")
                .email("kay4show@gmail.com")
                .department(department)
                .build();

        employeeRepository.save(employee2);

        Employee employee3 = Employee.builder()
                .firstName("Bayonle")
                .lastName("Apenku")
                .email("bayus@aol.com")
                .department(department2)
                .build();

        employeeRepository.save(employee3);

        //Act
        List<Employee> employees = employeeRepository.findAll();

        //Assert
        assertThat(employees).isNotNull()
                .contains(employee1, employee2, employee3);
    }

    @Test
    void shouldReturnMoreThanOneEmployee_WhenEmployeesAreFoundByDepartmentId() {

        //Arrange
        Department itDepartment = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(itDepartment);

        Department hrDepartment = Department.builder()
                .name("HR")
                .build();

        departmentRepository.save(hrDepartment);


        Employee employee1 = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(itDepartment)
                .build();

        employeeRepository.save(employee1);


        Employee employee2 = Employee.builder()
                .firstName("Kayode")
                .lastName("Apenku")
                .email("kay4show@gmail.com")
                .department(itDepartment)
                .build();

        employeeRepository.save(employee2);

        Employee employee3 = Employee.builder()
                .firstName("Bayonle")
                .lastName("Apenku")
                .email("bayus@aol.com")
                .department(hrDepartment)
                .build();

        employeeRepository.save(employee3);

        //Act
        List<Employee> employees = employeeRepository.findByDepartmentId(itDepartment.getId());

        //Assert
        assertThat(employees).isNotNull()
                .hasSize(2);
    }


    @Test
    void shouldReturnListContainingEmployees_WhenEmployeesAreFoundByDepartmentId() {

        //Arrange
        Department itDepartment = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(itDepartment);

        Department hrDepartment = Department.builder()
                .name("HR")
                .build();

        departmentRepository.save(hrDepartment);

        Employee employee1 = Employee.builder()
                .firstName("Adedayo")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(itDepartment)
                .build();

        employeeRepository.save(employee1);


        Employee employee2 = Employee.builder()
                .firstName("Kayode")
                .lastName("Apenku")
                .email("kay4show@gmail.com")
                .department(itDepartment)
                .build();

        employeeRepository.save(employee2);

        Employee employee3 = Employee.builder()
                .firstName("Bayonle")
                .lastName("Apenku")
                .email("bayus@aol.com")
                .department(hrDepartment)
                .build();

        employeeRepository.save(employee3);

        //Act
        List<Employee> employees = employeeRepository.findByDepartmentId(itDepartment.getId());

        //Assert
        assertThat(employees).isNotNull()
                .contains(employee1, employee2);
    }


}