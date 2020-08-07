package com.example.demo.controller;

import com.example.demo.mapper.EmployeeDataMapper;
import com.example.demo.model.dto.EmployeeDataDto;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.sender.KafkaProducer;
import com.example.demo.service.sender.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private EmployeeDataMapper employeeDataMapper;

    //api to get all employees from db
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getAllEmployees() {
        try {
            return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Employees not found!", HttpStatus.BAD_REQUEST);
        }
    }

    //api to add a employee to the db
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDataDto employeeDataDto) {
        try {
            employeeService.addEmployee(employeeDataDto);
            return new ResponseEntity<>("Employee added successfully!", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //api to get employee name of given id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEmployeeById(@RequestParam String id) {
        try {
            return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Employee not found!", HttpStatus.BAD_REQUEST);
        }
    }

    //api to delete employee of given id
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteEmployee(@RequestParam String id) {
        try {
            if (employeeService.deleteEmployee(id)) {
                return new ResponseEntity<>("Employee deleted successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Employee not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //rabbitmq update example
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateEmployeeByRMQ(@RequestParam("id") String id,
                                      @RequestBody EmployeeDataDto employeeDataDto) {
        rabbitMQSender.send(id, employeeDataDto);
        return "Employee data updated successfully!";
    }

    //kafka update example
    @RequestMapping(value = "/kafka/{id}", method = RequestMethod.PUT)
    public String updateEmployeeByKafka(@RequestParam("id") String id,
                                        @RequestBody EmployeeDataDto employeeDataDto) {
        kafkaProducer.updateEmployee(id, employeeDataDto);
        return "Employee data updated successfully!";
    }
}
