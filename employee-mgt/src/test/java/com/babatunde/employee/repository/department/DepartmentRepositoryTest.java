package com.babatunde.employee.repository.department;

import com.babatunde.employee.model.department.Department;
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
class DepartmentRepositoryTest {


    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void shouldReturnDepartment_WhenDepartmentIsSaved() {
        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        //Act
        Department savedDepartment = departmentRepository.save(department);

        //Assert
        assertThat(savedDepartment).isNotNull()
                .isEqualTo(department);

        assertThat(savedDepartment.getId()).isPositive();
    }

    @Test
    void shouldThrowException_WhenDepartmentNameIsAlreadyTaken() {
        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        departmentRepository.save(department);

        Department department2 = Department.builder()
                .name("IT")
                .build();

        //Act
        //Assert
        assertThatThrownBy(() -> departmentRepository.save(department2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldReturnDepartment_WhenDepartmentIsUpdated() {
        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        Department savedDepartment = departmentRepository.save(department);

        //Act
        savedDepartment.setName("Finance");
        Department updatedDepartment = departmentRepository.save(savedDepartment);

        //Assert
        assertThat(updatedDepartment).isNotNull()
                .isEqualTo(savedDepartment);

        assertThat(updatedDepartment.getId()).isPositive();
        assertThat(updatedDepartment.getName()).isEqualTo("Finance");
    }

    @Test
    void shouldReturnAllDepartments_WhenDepartmentsAreFound() {
        //Arrange
        Department itDepartment = Department.builder()
                .name("IT")
                .build();

        Department financeDepartment = Department.builder()
                .name("Finance")
                .build();

        departmentRepository.save(itDepartment);
        departmentRepository.save(financeDepartment);

        //Act
        Iterable<Department> departments = departmentRepository.findAll();

        //Assert
        assertThat(departments).isNotNull()
                .hasSize(2)
                .containsAll(List.of(itDepartment, financeDepartment));
    }

    @Test
    void shouldReturnDepartment_WhenDepartmentIsFoundById() {
        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        Department savedDepartment = departmentRepository.save(department);

        //Act
        Department departmentById = departmentRepository.findById(savedDepartment.getId()).orElse(null);

        //Assert
        assertThat(departmentById)
                .isNotNull()
                .isEqualTo(savedDepartment);
    }



    @Test
    void shouldReturnNull_WhenDepartmentIsDeleted() {
        //Arrange
        Department department = Department.builder()
                .name("IT")
                .build();

        Department savedDepartment = departmentRepository.save(department);

        //Act
        departmentRepository.delete(savedDepartment);

        //Assert
        Department departmentById = departmentRepository.findById(savedDepartment.getId()).orElse(null);
        assertThat(departmentById).isNull();
    }

}