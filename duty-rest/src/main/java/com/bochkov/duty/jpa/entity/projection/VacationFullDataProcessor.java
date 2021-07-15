package com.bochkov.duty.jpa.entity.projection;

import com.bochkov.duty.jpa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class VacationFullDataProcessor implements RepresentationModelProcessor<EntityModel<VacationFullData>> {

    @Autowired
    RepositoryEntityLinks links;

    @Override
    public EntityModel<VacationFullData> process(EntityModel<VacationFullData> model) {
        Link employee = links.linkForItemResource(Employee.class, model.getContent().getEmployee().getId()).withRel("employee");
        return model;
    }
}
