package com.example.demo.service;

import com.example.demo.mapper.EmployeeDataMapper;
import com.example.demo.model.bo.EmployeeDataBo;
import com.example.demo.model.dto.EmployeeDataDto;
import com.example.demo.service.transaction.EmployeeServiceTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class EmployeeServiceImpl implements EmployeeService {

    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeServiceTransaction employeeServiceTransaction;

    @Autowired
    private EmployeeDataMapper employeeDataMapper;

    @Autowired
    private RedisTemplate<String, EmployeeDataBo> redisTemplate;

    @Override
    public List<EmployeeDataDto> getAllEmployees() {
        return new ArrayList<>(
                employeeDataMapper.employeeBoFromDtoList(employeeServiceTransaction.getAllEmployees()));
    }

    /**
     * @param employeeDataDto add employee to local db and save it in redis db
     */
    @Override
    public void addEmployee(EmployeeDataDto employeeDataDto) {
        EmployeeDataBo emp = employeeServiceTransaction
                .addUpdateEmployee(employeeDataMapper.dtoToBo(employeeDataDto));
        employeeServiceTransaction.addToRedisDb(emp);
    }

    /**
     * @param id
     * @param employeeDataDto if no employee is present for given id return false
     *                        if present in redis cache delete it and save new data
     * @return
     */
    @Override
    public boolean updateEmployee(String id, EmployeeDataDto employeeDataDto) {
        final String key = "employee_" + id;
        final boolean hasKey = redisTemplate.hasKey(key);
        if (Objects.nonNull(employeeServiceTransaction.getEmployeeById(id))) {
            if (hasKey) {
                employeeServiceTransaction.deleteFromRedisDb(id);
            }
            employeeServiceTransaction.deleteEmployee(id);
            EmployeeDataBo emp = employeeServiceTransaction.addUpdateEmployee(employeeDataMapper.dtoToBo(employeeDataDto));
            employeeServiceTransaction.addToRedisDb(emp);
            return true;
        }
        return false;
    }

    /**
     * @param id if no employee is present for given id return false
     *           if present in redis cache delete it from there
     * @return
     */
    @Override
    public boolean deleteEmployee(String id) {
        final String key = "employee_" + id;
        final boolean hashKey = redisTemplate.hasKey(key);
        if (hashKey) {
            employeeServiceTransaction.deleteFromRedisDb(id);
        }
        if (Objects.nonNull(employeeServiceTransaction.getEmployeeById(id))) {
            employeeServiceTransaction.deleteEmployee(id);
            return true;
        }
        return false;
    }


    /**
     * @param id if present in redis cache get it from there
     *           else get it from local db and save in redis
     * @return
     */
    @Override
    public EmployeeDataDto getEmployeeById(String id) {
        final String key = "employee_" + id;
        final ValueOperations<String, EmployeeDataBo> operations = redisTemplate.opsForValue();
        final boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            final EmployeeDataBo employee = operations.get(key);
            logger.info("getEmployeeById() : cache employee >> " + employee);
            return employeeDataMapper.boToDto(employee);
        }
        final Optional<EmployeeDataBo> employee = Optional.ofNullable(employeeServiceTransaction.getEmployeeById(id));
        if (employee.isPresent()) {
            employeeServiceTransaction.addToRedisDb(employee.get());
            return employeeDataMapper.boToDto(employee.get());
        } else {
            throw new RuntimeException();
        }
    }
}
