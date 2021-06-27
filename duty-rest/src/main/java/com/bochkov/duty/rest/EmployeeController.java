package com.bochkov.duty.rest;

import com.bochkov.duty.jpa.entity.Employee;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("employees")
@CrossOrigin(origins = "*")
public class EmployeeController extends AbstractFinByLikeController<Employee> {

    public EmployeeController() {
        super(Employee.class, "lastName");
    }
}

