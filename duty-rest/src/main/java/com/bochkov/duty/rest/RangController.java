package com.bochkov.duty.rest;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("rangs")
@CrossOrigin(origins = "*")
public class RangController {
    @Autowired
    RangRepository rangRepository;

    @Autowired
    PagedResourcesAssembler<Rang> resourcesAssembler;

    @GetMapping(value = "findByLike", produces = {"application/json"})
    public PagedModel<EntityModel<Rang>> findByLike(@RequestParam(value = "search",defaultValue = "") String search, @RequestParam(value = "page", defaultValue = "0", required = false) Integer page, @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        Page<Rang> resultPage = rangRepository.findByLike(search, PageRequest.of(page, size), "id","name", "fullName");
        PagedModel<EntityModel<Rang>> model = resourcesAssembler.toModel(resultPage);
        return model;
    }
}
