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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    public static final String DEPARTMENT_NOT_FOUND = "Department not found";
    private final DepartmentDTOMapper mapper;

    private final DepartmentRepository repository;

    @Override
    public DepartmentDTO create(DepartmentCreationJSON creationJSON) {

        if (creationJSON.getName() == null) {
            throw new ApiBadRequestException("Name cannot be null");
        }

        repository.findByName(creationJSON.getName())
                .ifPresent(department -> {
                    throw new ApiResourceTakenException("Department with name " + creationJSON.getName() + " has already been created");
                });

        Department department = Department.builder()
                .name(creationJSON.getName())
                .build();

        Department savedDepartment = repository.save(department);

        return mapper.apply(savedDepartment);
    }

    @Override
    public List<DepartmentDTO> findAll() {
        List<Department> departments = repository.findAll();
        if (!departments.isEmpty()) {
            return departments.stream()
                    .map(mapper)
                    .toList();
        }
        return List.of();
    }

    @Override
    public DepartmentDTO update(Long id, DepartmentUpdateJSON updateJSON) {

        if (updateJSON.getName() == null) {
            throw new ApiBadRequestException("Name cannot be null");
        }

        Department department = repository.findById(id)
                .orElseThrow(() -> new ApiResourceNotFoundException(DEPARTMENT_NOT_FOUND));

        department.setName(updateJSON.getName());
        Department updatedDepartment = repository.save(department);

        return mapper.apply(updatedDepartment);
    }

    @Override
    public void delete(Long id) {
        Department department = repository.findById(id)
                .orElseThrow(() -> new ApiResourceNotFoundException(DEPARTMENT_NOT_FOUND));
        repository.delete(department);
    }

    @Override
    public DepartmentDTO findById(Long id) {
        Department department = repository.findById(id)
                .orElseThrow(() -> new ApiResourceNotFoundException(DEPARTMENT_NOT_FOUND));

        return mapper.apply(department);
    }

    @Override
    public Department internalFindByID(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiResourceNotFoundException(DEPARTMENT_NOT_FOUND));
    }
}
