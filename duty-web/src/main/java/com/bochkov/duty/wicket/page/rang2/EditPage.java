package com.bochkov.duty.wicket.page.rang2;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.jpa.crud.CrudEditPanel;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("rang2/edit")
public class EditPage extends BootstrapPage<Rang> {

    @SpringBean
    RangRepository repository;

    public EditPage() {
    }

    public EditPage(IModel<Rang> model) {
        super(model);
    }

    public EditPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new CrudEditPanel<Rang, Short>("content", Rang.class) {
            @Override
            protected RangRepository getRepository() {
                return repository;
            }

            @Override
            protected Component createInputPanel(String id, IModel<Rang> model) {
                return new FormComponentInput(id, model);
            }

            @Override
            protected AbstractLink createCloneButton(String id, IModel<Rang> model) {
                AbstractLink link = super.createCloneButton(id, model);
                link.setVisible(true).setEnabled(true);
                return link;
            }

            @Override
            public Rang copyDataForClone(Rang src, Rang dst) {
                Rang rang = super.copyDataForClone(src, dst);
                rang.setId(null);
                rang.setCreatedDate(null);
                return rang;
            }
        }.setAjax(true));
    }
}
