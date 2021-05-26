package com.bochkov.duty.rest;

import com.bochkov.duty.jpa.entity.ShiftType;
import com.bochkov.duty.jpa.repository.ShiftTypeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shiftTypes")
@CrossOrigin(origins = "*")
public class ShiftTypeController extends AbstractFinByLikeController<ShiftType> {

    public ShiftTypeController() {
        super(ShiftType.class, "name");
    }
}
