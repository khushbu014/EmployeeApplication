package com.example.demo.repo;

import com.example.demo.model.entity.EmployeeDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeDataEntity, String> {

}