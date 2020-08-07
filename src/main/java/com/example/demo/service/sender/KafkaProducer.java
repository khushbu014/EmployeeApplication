package com.example.demo.service.sender;

import com.example.demo.mapper.EmployeeDataMapper;
import com.example.demo.model.bo.EmployeeDataBo;
import com.example.demo.model.dto.EmployeeDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private static final String TOPIC = "employees";

    @Autowired
    private EmployeeDataMapper employeeDataMapper;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void updateEmployee(String id, EmployeeDataDto employeeDataDto) {
        logger.info("kafka message sent");
        EmployeeDataBo employeeDataBo = employeeDataMapper.dtoToBo(employeeDataDto);
        employeeDataBo.setId(id);
        kafkaTemplate.send(TOPIC, employeeDataDto);
    }

}
