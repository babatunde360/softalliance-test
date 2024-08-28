package com.babatunde.employee.service.department;

import com.babatunde.employee.model.department.Department;
import com.babatunde.employee.model.department.request.DepartmentCreationJSON;
import com.babatunde.employee.model.department.request.DepartmentUpdateJSON;
import com.babatunde.employee.model.department.response.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    //    Service create
    DepartmentDTO create(DepartmentCreationJSON creationJSON);

    //     findAll
    List<DepartmentDTO> findAll();

    //   service update
    DepartmentDTO update(Long id, DepartmentUpdateJSON updateJSON);

    // service delete
    void delete(Long id);

    //    service find by id
    DepartmentDTO findById(Long id);

    Department internalFindByID(Long id);

}
