package com.babatunde.employee.service.department;

import com.babatunde.employee.exception.ApiBadRequestException;
import com.babatunde.employee.exception.ApiResourceNotFoundException;
import com.babatunde.employee.exception.ApiResourceTakenException;
import com.babatunde.employee.model.department.Department;
import com.babatunde.employee.model.department.request.DepartmentCreationJSON;
import com.babatunde.employee.model.department.request.DepartmentUpdateJSON;
import com.babatunde.employee.model.department.response.DepartmentDTO;
import com.babatunde.employee.model.department.response.DepartmentDTOMapper;
import com.babatunde.employee.repository.department.DepartmentRepository;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    DepartmentDTOMapper mapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department itDepartment;
    private DepartmentDTO itDepartmentDTO;
    private DepartmentDTO financeDepartmentDTO;
    private List<Department> departments;


    @BeforeEach
    void setUp() {
        itDepartment = Department.builder()
                .id(1L)
                .name("IT").build();

        Department financeDepartment = Department.builder()
                .id(2L)
                .name("Finance").build();

        itDepartmentDTO = DepartmentDTO.builder()
                .name("IT")
                .id(1L)
                .build();

        financeDepartmentDTO = DepartmentDTO.builder()
                .name("Finance")
                .id(2L)
                .build();

        departments = List.of(itDepartment, financeDepartment);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(departmentRepository, mapper);
    }

    @Test
    void shouldThrowBadRequestException_WhenDepartmentCreationIsCalledWithNullName() {
        DepartmentCreationJSON departmentCreationJSON = new DepartmentCreationJSON(null);

        assertThatThrownBy(() -> departmentService.create(departmentCreationJSON))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessage("Name cannot be null");
    }

    @Test
    void shouldReturnDepartmentDTO_WhenDepartmentIsSaved() {
        DepartmentCreationJSON departmentCreationJSON = new DepartmentCreationJSON("IT");

        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(itDepartment);
        when(mapper.apply(itDepartment)).thenReturn(itDepartmentDTO);

        DepartmentDTO result = departmentService.create(departmentCreationJSON);

        assertThat(result).isNotNull().isEqualTo(itDepartmentDTO);
    }

    @Test
    void shouldThrowResourceTakenException_WhenDepartmentNameIsAlreadyTaken() {
        DepartmentCreationJSON departmentCreationJSON = new DepartmentCreationJSON("IT");

        when(departmentRepository.findByName("IT")).thenReturn(Optional.of(itDepartment));

        assertThatThrownBy(() -> departmentService.create(departmentCreationJSON))
                .isInstanceOf(ApiResourceTakenException.class)
                .hasMessage("Department with name IT has already been created");
    }

    @Test
    void shouldThrowBadRequestException_WhenDepartmentUpdateIsCalledWithNullName() {
        DepartmentUpdateJSON departmentUpdateJSON = new DepartmentUpdateJSON(null);

        assertThatThrownBy(() -> departmentService.update(1L, departmentUpdateJSON))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessage("Name cannot be null");
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenDepartmentUpdateIsCalledWithWrongId() {
        DepartmentUpdateJSON departmentUpdateJSON = new DepartmentUpdateJSON("IT");

        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.update(1L, departmentUpdateJSON))
                .isInstanceOf(ApiResourceNotFoundException.class)
                .hasMessage("Department not found");
    }

    @Test
    void shouldReturnUpdatedDepartmentDTO_WhenDepartmentUpdateIsCalledWithCorrectId() {
        DepartmentUpdateJSON departmentUpdateJSON = new DepartmentUpdateJSON("HR");

        Department updatedDepartment = Department.builder()
                .name("HR")
                .build();

        DepartmentDTO updatedDepartmentDTO = DepartmentDTO.builder()
                .name("HR")
                .id(1L)
                .build();

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(itDepartment));
        when(departmentRepository.save(Mockito.any(Department.class))).thenReturn(updatedDepartment);
        when(mapper.apply(updatedDepartment)).thenReturn(updatedDepartmentDTO);

        DepartmentDTO result = departmentService.update(1L, departmentUpdateJSON);

        assertThat(result).isNotNull().isEqualTo(updatedDepartmentDTO);
    }

    @Test
    void shouldReturnEmptyList_WhenNoDepartmentIsFound() {
        when(departmentRepository.findAll()).thenReturn(List.of());

        assertThat(departmentService.findAll()).isEmpty();
    }

    @Test
    void shouldReturnListOfDepartmentDTO_WhenDepartmentsAreFound() {
        when(departmentRepository.findAll()).thenReturn(departments);
        when(mapper.apply(Mockito.any(Department.class))).thenAnswer(invocation -> {
            Department department = invocation.getArgument(0);
            if (department.getName().equals("IT")) {
                return itDepartmentDTO;
            } else if (department.getName().equals("Finance")) {
                return financeDepartmentDTO;
            }
            return null;
        });

        List<DepartmentDTO> result = departmentService.findAll();

        assertThat(result).isNotEmpty().containsExactly(itDepartmentDTO, financeDepartmentDTO);
    }

    @Test
    void shouldDeleteDepartment_WhenDepartmentExists() {
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(itDepartment));

        departmentService.delete(departmentId);

        Mockito.verify(departmentRepository).delete(itDepartment);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenDepartmentDoesNotExist() {
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.delete(departmentId))
                .isInstanceOf(ApiResourceNotFoundException.class)
                .hasMessage("Department not found");
    }

    @Test
    void shouldReturnDepartmentDTO_WhenDepartmentIsFoundById() {
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(itDepartment));
        when(mapper.apply(itDepartment)).thenReturn(itDepartmentDTO);

        DepartmentDTO result = departmentService.findById(departmentId);

        assertThat(result).isNotNull().isEqualTo(itDepartmentDTO);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenDepartmentIsNotFoundById() {
        Long departmentId = 1L;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.findById(departmentId))
                .isInstanceOf(ApiResourceNotFoundException.class)
                .hasMessage("Department not found");
    }
}