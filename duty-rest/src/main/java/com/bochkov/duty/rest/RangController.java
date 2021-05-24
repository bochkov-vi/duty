package com.bochkov.duty.rest;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("rangs")
@CrossOrigin(origins = "*")
@Slf4j
public class RangController {
    @Autowired
    RangRepository rangRepository;


    @Autowired
    PagedResourcesAssembler<Rang> resourcesAssembler;

    @Autowired
    RepositoryEntityLinks links;

    RepresentationModelAssembler<Rang,EntityModel<Rang>> assembler = new SimpleRepresentationModelAssembler<Rang>() {
        @Override
        public void addLinks(EntityModel<Rang> resource) {
            log.debug("Add entity model links to {}",resource);
            resource.add(links.linkToItemResource(Rang.class,resource.getContent().getId()));
        }

        @Override
        public void addLinks(CollectionModel<EntityModel<Rang>> resources) {
            log.debug("Add collection model links to {}",resources);
        }
    };

    @GetMapping(value = "findByLike", produces = {"application/json"})
    public PagedModel<EntityModel<Rang>> findByLike(@RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "page", defaultValue = "0", required = false) Integer page, @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        Page<Rang> resultPage = rangRepository.findByLike(search, PageRequest.of(page, size), "id", "name", "fullName");
        PagedModel<EntityModel<Rang>> model = resourcesAssembler.toModel(resultPage,assembler);
        return model;
    }
}
