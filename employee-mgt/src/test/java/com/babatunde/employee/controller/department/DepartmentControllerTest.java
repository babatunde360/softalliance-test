package com.babatunde.employee.controller.department;

import com.babatunde.employee.model.department.response.DepartmentDTO;
import com.babatunde.employee.model.response.ResponseBody;
import com.babatunde.employee.service.department.DepartmentService;
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

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith({MockitoExtension.class})
@ContextConfiguration(classes = {DepartmentController.class})
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    private ObjectMapper objectMapper;

    private DepartmentDTO itDepartmentDTO;
    private DepartmentDTO financeDepartmentDTO;
    private List<DepartmentDTO> departmentDTOs;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        itDepartmentDTO = DepartmentDTO.builder()
                .name("IT")
                .id(1L)
                .build();

        financeDepartmentDTO = DepartmentDTO.builder()
                .name("Finance")
                .id(2L)
                .build();

        departmentDTOs = List.of(itDepartmentDTO, financeDepartmentDTO);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(departmentService);
    }

    @Test
    void shouldReturnDepartmentDTO_whenDepartmentIsCreated() throws Exception {

        given(departmentService.create(ArgumentMatchers.any())).willReturn(itDepartmentDTO);

        ResultActions result = mockMvc.perform(post("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ResponseBody<>(true,
                        "Department created successfully", itDepartmentDTO))));

        result.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(itDepartmentDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(itDepartmentDTO.getId()));
    }


    @Test
    void shouldReturnAllDepartments_whenDepartmentsAreRetrieved() throws Exception {

        given(departmentService.findAll()).willReturn(departmentDTOs);

        ResultActions result = mockMvc.perform(get("/api/v1/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ResponseBody<>(true,
                        "Departments retrieved successfully", departmentDTOs))));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].size()").value(departmentDTOs.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(itDepartmentDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(itDepartmentDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name").value(financeDepartmentDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(financeDepartmentDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Departments retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }


    @Test
    void shouldReturnUpdatedDepartment_whenDepartmentIsUpdated() throws Exception {

        given(departmentService.update(any(), any())).willReturn(itDepartmentDTO);

        ResultActions result = mockMvc.perform(put("/api/v1/departments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ResponseBody<>(true,
                        "Department updated successfully", itDepartmentDTO))));

        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(itDepartmentDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(itDepartmentDTO.getId()));
    }


    @Test
    void shouldReturnNoContent_whenDepartmentIsDeleted() throws Exception {

        ResultActions result = mockMvc.perform(delete("/api/v1/departments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ResponseBody<>(true,
                        "Department deleted successfully"))));

        result.andExpect(status().isNoContent());
    }

}