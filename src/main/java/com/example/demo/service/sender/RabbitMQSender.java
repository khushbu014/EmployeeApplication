package com.example.demo.service.sender;

import com.example.demo.mapper.EmployeeDataMapper;
import com.example.demo.model.bo.EmployeeDataBo;
import com.example.demo.model.dto.EmployeeDataDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private EmployeeDataMapper employeeDataMapper;

    @Value("${employee.rabbitmq.exchange}")
    private String exchange;

    @Value("${employee.rabbitmq.routingkey}")
    private String routingkey;

    public void send(String id, EmployeeDataDto emp) {
        EmployeeDataBo employeeDataBo = employeeDataMapper.dtoToBo(emp);
        employeeDataBo.setId(id);
        rabbitTemplate.convertAndSend(exchange, routingkey, employeeDataBo);
        System.out.println("Send msg = " + emp);
    }
}
