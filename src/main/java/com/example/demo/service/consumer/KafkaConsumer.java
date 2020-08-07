package com.example.demo.service.consumer;

import com.example.demo.mapper.EmployeeDataMapper;
import com.example.demo.model.bo.EmployeeDataBo;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.sender.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private static final String TOPIC = "employees";

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeDataMapper employeeDataMapper;

    @KafkaListener(topics = TOPIC)
    public void updateEmployee(EmployeeDataBo employeeDataBo) {
        logger.info("kafka consumer : message received", employeeDataBo.getName());
        if (employeeService.updateEmployee(employeeDataBo.getId(), employeeDataMapper.boToDto(employeeDataBo))) {
            logger.info("employee updated successfully!");
        } else {
            logger.info("employee not found!");
        }

    }
}
