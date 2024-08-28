package com.babatunde.employee.controller.employee;

import com.babatunde.employee.model.department.Department;
import com.babatunde.employee.model.employee.Employee;
import com.babatunde.employee.model.employee.response.EmployeeDTO;
import com.babatunde.employee.model.response.ResponseBody;
import com.babatunde.employee.service.employee.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {EmployeeController.class})
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private ObjectMapper objectMapper;

    private Department itDepartment;
    private Department financeDepartment;
    private Employee employee1;
    private Employee employee2;
    private Employee employee3;
    private EmployeeDTO employeeDTO1;
    private EmployeeDTO updatedEmployeeDTO1;
    private EmployeeDTO employeeDTO2;
    private EmployeeDTO employeeDTO3;
    private List<Employee> employees;
    private List<EmployeeDTO> employeeDTOs;
    private List<EmployeeDTO> departmentEmployeeDTOs;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        itDepartment = Department.builder()
                .id(1L)
                .name("IT").build();

        financeDepartment = Department.builder()
                .id(2L)
                .name("Finance").build();

        employee1 = Employee.builder().firstName("Adewale")
                .lastName("Apenku")
                .email("apenku4life@gmail.com")
                .department(itDepartment).build();

        employee3 = Employee.builder().firstName("Trafalgar")
                .lastName("Law")
                .email("trafalgardwaterlaw@gmai.com")
                .department(itDepartment).build();

        employee2 = Employee.builder().firstName("Adedayo")
                .email("adedayo@gmail.com")
                .lastName("Apenku").department(financeDepartment).build();

        employeeDTO1 = EmployeeDTO.builder().firstName("Adewale")
                .id(1L).lastName("Apenku")
                .email("apenku4life@gmail.com")
                .departmentName("IT").build();

        updatedEmployeeDTO1 = EmployeeDTO.builder().firstName("Adewale")
                .id(1L).lastName("Apenku")
                .email("apenku2001@gmail.com")
                .departmentName("IT").build();

        employeeDTO2 = EmployeeDTO.builder().firstName("Adewale")
                .lastName("Apenku")
                .email("adedayo@gmail.com")
                .departmentName("Finance").id(2L).build();

        employeeDTO3 = EmployeeDTO.builder().firstName("Trafalgar")
                .lastName("Law")
                .email("trafalgardwaterlaw@gmai.com")
                .departmentName("IT").id(3L).build();

        employees = List.of(employee1, employee2);
        employeeDTOs = List.of(employeeDTO1, employeeDTO2);
        departmentEmployeeDTOs = List.of(employeeDTO1, employeeDTO3);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(employeeService);
    }

    @Test
    void shouldReturnDepartmentDTO_whenDepartmentIsCreated() throws Exception {

        given(employeeService.create(ArgumentMatchers.any())).willReturn(employeeDTO1);

        ResultActions result = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(
                                new ResponseBody<>(true, "Employee created successfully", employeeDTO1))));

        result.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value(employeeDTO1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value(employeeDTO1.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(employeeDTO1.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.departmentName").value(employeeDTO1.getDepartmentName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(employeeDTO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andDo(mvcResult -> log.info(mvcResult.getResponse().getContentAsString()));
    }


    @Test
    void shouldReturnUpdatedEmployeeDTO_whenEmployeeIsUpdated() throws Exception {

        given(employeeService.update(any(), any())).willReturn(updatedEmployeeDTO1);

        ResultActions result = mockMvc.perform(put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(
                                new ResponseBody<>(true, "Employee updated successfully", updatedEmployeeDTO1))));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value(updatedEmployeeDTO1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value(updatedEmployeeDTO1.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(updatedEmployeeDTO1.getEmail()));

    }

    @Test
    void shouldReturnAllEmployees_whenGetAllEmployeesIsCalled() throws Exception {

        given(employeeService.findAll()).willReturn(employeeDTOs);

        ResultActions result = mockMvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(
                                new ResponseBody<>(true, "Employees retrieved successfully", employeeDTOs))));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(employeeDTOs.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].firstName").value(employeeDTOs.get(0).getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].lastName").value(employeeDTOs.get(0).getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email").value(employeeDTOs.get(0).getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].departmentName").value(employeeDTOs.get(0).getDepartmentName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(employeeDTOs.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employees retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }


    @Test
    void shouldReturnEmployeeDTO_whenGetEmployeeIsCalledById() throws Exception {

        given(employeeService.findById(1L)).willReturn(employeeDTO1);

        ResultActions result = mockMvc.perform(get("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(
                                new ResponseBody<>(true, "Employee retrieved successfully", employeeDTO1))));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value(employeeDTO1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value(employeeDTO1.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(employeeDTO1.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.departmentName").value(employeeDTO1.getDepartmentName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(employeeDTO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturnNoContent_whenEmployeeIsDeleted() throws Exception {

        ResultActions result = mockMvc.perform(delete("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(
                                new ResponseBody<>(true, "Employee deleted successfully"))));

        result.andExpect(status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employee deleted successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    void shouldReturnEmployeesDTOInTheSameDepartment_whenGetEmployeeByDepartmentIsCalled() throws Exception {

        given(employeeService.findByDepartmentId(1L)).willReturn(departmentEmployeeDTOs);

        ResultActions result = mockMvc.perform(get("/api/v1/employees/department/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(
                                new ResponseBody<>(true, "Employees retrieved successfully", departmentEmployeeDTOs))));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(departmentEmployeeDTOs.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].departmentName").value(itDepartment.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].departmentName").value(itDepartment.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Employees retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

}