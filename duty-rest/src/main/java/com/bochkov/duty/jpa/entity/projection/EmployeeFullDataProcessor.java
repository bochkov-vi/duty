package com.bochkov.duty.jpa.entity.projection;

import com.bochkov.duty.jpa.entity.Rang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFullDataProcessor implements RepresentationModelProcessor<EntityModel<EmployeeFullData>> {

    @Autowired
    RepositoryEntityLinks links;

    @Override
    public EntityModel<EmployeeFullData> process(EntityModel<EmployeeFullData> model) {
        Link rang = links.linkForItemResource(Rang.class, model.getContent().getRang().getId()).withRel("rang");
       // model.add(rang);
        return model;
    }
}
