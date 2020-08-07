package com.example.demo.service.transaction;

import com.example.demo.mapper.EmployeeDataMapper;
import com.example.demo.model.bo.EmployeeDataBo;
import com.example.demo.model.entity.EmployeeDataEntity;
import com.example.demo.repo.EmployeeRepository;
import com.example.demo.service.EmployeeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class EmployeeServiceTransaction {

    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDataMapper employeeDataMapper;

    @Autowired
    private RedisTemplate<String, EmployeeDataBo> redisTemplate;

    public List<EmployeeDataBo> getAllEmployees() {
        return employeeDataMapper.employeeBoFromEntityList(employeeRepository.findAll());
    }

    public EmployeeDataBo getEmployeeById(String id) {
        if (employeeRepository.findById(id).isPresent()) {
            return employeeDataMapper.entityToBo(employeeRepository.findById(id).get());
        } else return null;
    }

    public EmployeeDataBo addUpdateEmployee(EmployeeDataBo employeeDataBo) {
        EmployeeDataEntity employeeDataEntity =
                employeeRepository.save(employeeDataMapper.boToEntity(employeeDataBo));
        employeeDataBo.setId(employeeDataEntity.getId());
        return employeeDataBo;
    }

    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    public void addToRedisDb(EmployeeDataBo employeeDataBo) {
        final String key = "employee_" + employeeDataBo.getId();
        final ValueOperations<String, EmployeeDataBo> operations = redisTemplate.opsForValue();
        operations.set(key, employeeDataBo, 10, TimeUnit.MINUTES);
        logger.info("addToRedisDb() cache employee inserted >> " + employeeDataBo.getId());
    }

    public void deleteFromRedisDb(String id) {
        final String key = "employee_" + id;
        redisTemplate.delete(key);
        logger.info("deleteFromRedisDb() : cached employee deleted >> " + id);
    }

}
