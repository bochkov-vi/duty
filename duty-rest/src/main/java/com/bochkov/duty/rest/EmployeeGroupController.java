package com.bochkov.duty.rest;

import com.bochkov.duty.jpa.entity.EmployeeGroup;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("employeeGroups")
@CrossOrigin(origins = "*")
public class EmployeeGroupController extends AbstractFinByLikeController<EmployeeGroup> {

    public EmployeeGroupController() {
        super(EmployeeGroup.class, "name");
    }
}
