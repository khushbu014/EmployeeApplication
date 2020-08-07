package com.example.demo.service;

import com.example.demo.model.dto.EmployeeDataDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {

    List<EmployeeDataDto> getAllEmployees();

    EmployeeDataDto getEmployeeById(String id);

    void addEmployee(EmployeeDataDto employeeDataBo);

    boolean updateEmployee(String id, EmployeeDataDto employeeDataDto);

    boolean deleteEmployee(String id);
}

