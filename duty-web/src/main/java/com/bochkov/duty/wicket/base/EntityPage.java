package com.bochkov.duty.wicket.base;

import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.component.toast.ToastFeedbackPanel;
import com.bochkov.wicket.data.model.PersistableModel;
import com.bochkov.wicket.data.provider.PersistableDataProvider;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class EntityPage<T extends Persistable<ID>, ID extends Serializable> extends BootstrapPage<T> {
    static String MODAL_CONTENT_ID = "modal-content";

    @Accessors(chain = true)
    public final String MODAL_SMALL = "modal-sm";

    public final String MODAL_NORMAL = "";

    public final String MODAL_LARGE = "modal-lg";

    public final String MODAL_EXTRA_LARGE = "modal-xl";

    @Getter
    @Setter
    String modalSize = MODAL_NORMAL;

    WebMarkupContainer modal = new WebMarkupContainer("modal");

    WebMarkupContainer modalContainer = new WebMarkupContainer("modal-container");

    WebMarkupContainer emptyContent = new WebMarkupContainer(MODAL_CONTENT_ID);

    ToastFeedbackPanel feedback = new ToastFeedbackPanel("feedback");

    WebMarkupContainer modalSizer = new WebMarkupContainer("modal-size");

    Fragment formInputFragment;

    Fragment formDeleteFragment;

    Form<T> editForm;

    WebMarkupContainer table;

    public EntityPage() {
        super();
        setModel(PersistableModel.of(id -> {
            return getRepository().findById(id);
        }));
    }

    public EntityPage(T entity) {
        this();
        setModelObject(entity);
    }

    protected void setModalContent(Component component) {
        if (!Objects.equals(component.getId(), MODAL_CONTENT_ID)) {
            throw new RuntimeException(String.format("component with id %s not eq %s", component.getId(), MODAL_CONTENT_ID));
        }
        modalContainer.replace(component);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        feedback.setOutputMarkupId(true);
        feedback.setDelay(30000L);
        add(feedback);
        add(table = table("table"));
        modal.setOutputMarkupId(true);
        modalContainer.setOutputMarkupId(true);
        add(modal);
        modalSizer.add(new ClassAttributeModifier() {
            @Override
            protected Set<String> update(Set<String> oldClasses) {
                oldClasses.add(modalSize);
                return oldClasses;
            }
        });
        modal.add(modalSizer);
        modalSizer.add(modalContainer);

        modalContainer.add(emptyContent);

        formInputFragment = new Fragment(MODAL_CONTENT_ID, "form-input-fragment", getPage());
        formInputFragment.add(editForm = saveForm("form-save"));
        formDeleteFragment = new Fragment(MODAL_CONTENT_ID, "form-delete-fragment", getPage());
        formDeleteFragment.add(deleteForm("form-delete"));
        add(new AjaxLink<Void>("btn-new-row") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                editForm.setModelObject(newInstance());
                showModal(target);
                setModalContent(formInputFragment);
            }
        });
    }

    abstract protected <R extends JpaRepository<T, ID> & JpaSpecificationExecutor<T>> R getRepository();

    protected WebMarkupContainer table(String id) {
        List<IColumn<T, String>> columns = Lists.newArrayList(columns());
        columns.add(editColumn());
        //columns.add(deleteColumn());
        DataTable<T, String> dataTable = new DefaultDataTable<>(id, columns, dataProvider(), 50);
        dataTable.setOutputMarkupId(true);
        return dataTable;
    }

    IColumn<T, String> editColumn() {
        return new HeaderlessColumn<T, String>() {
            @Override
            public void populateItem(Item<ICellPopulator<T>> item, String s, IModel<T> iModel) {
                item.add(new Fragment(s, "edit-panel-fragment", getPage()).add(
                        new AjaxLink<T>("edit", iModel) {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                onEditLinkClick(target, getModel());
                            }
                        }
                        ).add(new AjaxLink<T>("delete", iModel) {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                onDeleteLinkClick(target, getModel());
                            }
                        })
                );
            }
        };
    }

    public void onEditLinkClick(AjaxRequestTarget target, IModel<T> model) {
        modalContainer.replace(formInputFragment);
        showModal(target);
        editForm.setModelObject(model.getObject());
    }

    public void onDeleteLinkClick(AjaxRequestTarget target, IModel<T> model) {
        modalContainer.replace(formDeleteFragment);
        showModal(target);
        EntityPage.this.setModelObject(model.getObject());
    }

    IColumn<T, String> deleteColumn() {
        return new HeaderlessColumn<T, String>() {
            @Override
            public void populateItem(Item<ICellPopulator<T>> item, String s, IModel<T> iModel) {
                item.add(new Fragment(s, "delete-link-fragment", getPage()).add(
                        new AjaxLink<T>("link", iModel) {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                modalContainer.replace(formDeleteFragment);
                                showModal(target);
                                EntityPage.this.setModelObject(this.getModelObject());
                            }
                        }
                ));
            }
        };
    }

    protected List<IColumn<T, String>> columns() {
        return Lists.newArrayList();
    }

    protected ISortableDataProvider<T, String> dataProvider() {
        return PersistableDataProvider.of(this::getRepository, this::createSpecification);
    }

    public Specification<T> createSpecification() {
        return null;
    }

    public void showModal(AjaxRequestTarget target) {
        target.appendJavaScript(String.format("$('#%s').modal('show')", modal.getMarkupId()));
        target.add(modalContainer);
    }

    public void hideModal(AjaxRequestTarget target) {
        target.appendJavaScript(String.format("$('#%s').modal('hide')", modal.getMarkupId()));
        target.add(modalContainer);
    }

    Form<T> saveForm(String id) {
        Form<T> form = new Form<T>(id, new CompoundPropertyModel<>(getModel())) {
            @Override
            protected void onSubmit() {

            }
        };
        form.add(new AjaxButton("btn-save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                try {
                    save(EntityPage.this.getModelObject());
                    success(new StringResourceModel("message.entity.saved", this, EntityPage.this.getModel()).getString());
                    hideModal(target);
                    target.add(table);
                } catch (Exception e) {
                    error(e.getLocalizedMessage());
                }
                target.add(feedback);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedback);
            }
        });
        form.add(createInputPanel("input-panel", form.getModel()));
        return form;
    }

    Form<T> deleteForm(String id) {
        Form<T> deleteForm = new Form<>(id, new CompoundPropertyModel<>(getModel()));
        deleteForm.add(createDetailsPanel("details-panel", deleteForm.getModel()));
        deleteForm.add(new AjaxButton("btn-delete") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (deleteForm.getModel().isPresent().getObject()) {
                    T entity = deleteForm.getModelObject();
                    delete(target, entity);
                    warn(getString("message.entity.deleted", deleteForm.getModel()));
                    hideModal(target);
                    target.add(table);
                    target.add(feedback);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
            }
        });
        return deleteForm;
    }

    protected WebMarkupContainer createInputPanel(String id, IModel<T> model) {
        return new GenericPanel<>(id, model);
    }

    protected GenericPanel<T> createDetailsPanel(String id, IModel<T> model) {
        return new DetailsPanel<>(id, model, ImmutableList.of("id", "name"), "");
    }

    public void save(T entity) {
        entity = getRepository().saveAndFlush(entity);
        setModelObject(entity);
    }

    public void delete(AjaxRequestTarget target, T entity) {
        getRepository().delete(entity);
        setModelObject(null);
    }

    public void create() {

    }

    abstract protected T newInstance();


}
