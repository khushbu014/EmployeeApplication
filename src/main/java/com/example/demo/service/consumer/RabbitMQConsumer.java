package com.example.demo.service.consumer;

import com.example.demo.mapper.EmployeeDataMapper;
import com.example.demo.model.bo.EmployeeDataBo;
import com.example.demo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeDataMapper employeeDataMapper;

    Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = "${employee.rabbitmq.queue}")
    public void receivedMessage(EmployeeDataBo emp) {
        logger.info("Received Message From RabbitMQ: " + emp);
        if (employeeService.updateEmployee(emp.getId(), employeeDataMapper.boToDto(emp))) {
            logger.info("employee updated successfully!");
        } else {
            logger.info("employee not found!");
        }
    }
}
