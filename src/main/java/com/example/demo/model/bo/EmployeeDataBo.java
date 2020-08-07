package com.example.demo.model.bo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id", scope = EmployeeDataBo.class)
public class EmployeeDataBo implements Serializable {

    String id;
    String email;
    String name;

    public EmployeeDataBo(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public EmployeeDataBo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
