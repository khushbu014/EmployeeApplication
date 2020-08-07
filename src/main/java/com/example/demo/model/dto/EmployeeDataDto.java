package com.example.demo.model.dto;

public class EmployeeDataDto {

    String email;
    String name;

    public EmployeeDataDto() {
    }

    public EmployeeDataDto(String id, String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
