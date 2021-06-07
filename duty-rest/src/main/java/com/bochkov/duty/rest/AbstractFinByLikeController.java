package com.bochkov.duty.rest;

import com.bochkov.findbylike.FindByLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Slf4j
public class AbstractFinByLikeController<T> {
    @Autowired
    FindByLikeRepository<T> repository;

    Class<T> domainClss;

    @Autowired
    PagedResourcesAssembler<T> resourcesAssembler;

    @Autowired
    RepositoryEntityLinks links;

    @Autowired
    Repositories repositories;

    EntityInformation<T, ?> entityInformation;

    String[] likeProperties;


    RepresentationModelAssembler<T, EntityModel<T>> assembler = new SimpleRepresentationModelAssembler<T>() {
        @Override
        public void addLinks(EntityModel<T> resource) {
            AbstractFinByLikeController.this.addLinks(resource);
        }

        @Override
        public void addLinks(CollectionModel<EntityModel<T>> resources) {
            AbstractFinByLikeController.this.addLinks(resources);
        }
    };

    public AbstractFinByLikeController(Class<T> domainClss, String... likeProperties) {
        this.domainClss = domainClss;
        this.likeProperties = likeProperties;
    }

    @PostConstruct
    public void postConstruct() {
        this.entityInformation = repositories.getEntityInformationFor(domainClss);
    }

    public void addLinks(EntityModel<T> resource) {
        Optional.ofNullable(resource).map(EntityModel::getContent).map(e -> entityInformation.getId(e)).ifPresent((id) -> {
            resource.add(links.linkToItemResource(domainClss, id));
            resource.add(links.linkForItemResource(domainClss, id).withRel("self"));
        });
    }

    public void addLinks(CollectionModel<EntityModel<T>> resources) {

    }

    @GetMapping(value = "findByLike", produces = {"application/json"})
    public PagedModel<EntityModel<T>> findByLike(@RequestParam(value = "search", defaultValue = "") String search, @RequestParam(value = "page", defaultValue = "0", required = false) Integer page, @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        Page<T> resultPage = repository.findByLike(search, PageRequest.of(page, size), likeProperties);
        PagedModel<EntityModel<T>> model = resourcesAssembler.toModel(resultPage, assembler);
        return model;
    }
}
