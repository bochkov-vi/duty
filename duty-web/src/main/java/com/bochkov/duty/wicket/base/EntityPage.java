package com.bochkov.duty.wicket.base;

import com.bochkov.duty.wicket.WicketDutyApplication;
import com.bochkov.duty.wicket.page.BootstrapPage;
import com.bochkov.wicket.data.provider.PersistableDataProvider;
import com.bochkov.wicket.jpa.model.PersistableModel;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.Application;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.beans.BeanUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.support.Repositories;

import java.beans.FeatureDescriptor;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class EntityPage<T extends Persistable<ID>, ID extends Serializable> extends BootstrapPage<T> {

    static String MODAL_CONTENT_ID = "modal-content";

    @Accessors(chain = true)
    public final String MODAL_SMALL = "modal-sm";

    public final String MODAL_NORMAL = "";

    public final String MODAL_LARGE = "modal-lg";

    public final String MODAL_EXTRA_LARGE = "modal-xl";

    @Getter
    private final Class<T> entityClass;

    @Getter
    private final Class<ID> idClass;


    @Getter
    @Setter
    String modalSize = MODAL_NORMAL;

    WebMarkupContainer modal = new WebMarkupContainer("modal");

    WebMarkupContainer modalContainer = new WebMarkupContainer("modal-container");

    WebMarkupContainer emptyContent = new WebMarkupContainer(MODAL_CONTENT_ID);

    Component feedback = new FeedbackPanel("feedback");

    WebMarkupContainer modalSizer = new WebMarkupContainer("modal-size");

    Fragment formInputFragment;

    Fragment formDeleteFragment;

    Form<T> editForm;

    @Getter
    @Setter
    @Accessors(chain = true)
    boolean modalMode = true;

    @Getter
    @Setter
    @Accessors(chain = true)
    boolean editMode = false;

    protected WebMarkupContainer tableContainer = new WebMarkupContainer("table-container") {
        @Override
        public boolean isVisible() {
            return !editMode;
        }
    };

    WebMarkupContainer editFormContainer = new WebMarkupContainer("edit-form-container") {
        @Override
        public boolean isVisible() {
            return editMode && !modalMode;
        }
    };

    WebMarkupContainer table;


    public EntityPage(Class<T> entityClass) {
        this.entityClass = entityClass;
        EntityInformation<T, ID> entityInformation = new Repositories(WicketDutyApplication.get().getApplicationContext()).getEntityInformationFor(entityClass);
        this.idClass = entityInformation.getIdType();
        setModel(model((T) null));
    }

    public EntityPage(T entity) {
        this((Class<T>) entity.getClass());
        setModel(model(entity));
    }

    public EntityPage(IModel<T> model, Class<T> entityClass) {
        this(entityClass);
        setModel(model);
    }


    public EntityPage(PageParameters parameters, Class<T> entityClass) {
        super(parameters);
        this.entityClass = entityClass;
        EntityInformation<T, ID> entityInformation = new Repositories(WicketDutyApplication.get().getApplicationContext()).getEntityInformationFor(entityClass);
        this.idClass = entityInformation.getIdType();
        ID pk = EntityPage.extractFromParameters(parameters, entityInformation.getIdType());
        IModel<T> model = model(pk);
        setModel(model);
        editMode = true;
    }

    public static <ID extends Serializable> PageParameters pageParameters(ID id, Class<ID> _class) {
        String parameter = Optional.ofNullable(id).map(val -> Application.get().getConverterLocator().getConverter(_class).convertToString(val, Session.get().getLocale())).orElse(null);
        return new PageParameters().set(0, parameter);
    }

    public static <ID extends Serializable> PageParameters pageParameters(IModel<ID> id, Class<ID> _class) {
        return pageParameters(id.getObject(), _class);
    }

    public static <ID extends Serializable> ID extractFromParameters(PageParameters pageParameters, Class<ID> _class) {
        String value = Optional.ofNullable(pageParameters.get(0)).map(stringValue -> stringValue.toOptionalString()).orElseGet(
                () -> Optional.ofNullable(pageParameters.get("id")).map(stringValue -> stringValue.toOptionalString()).orElse(null));
        ID id = Application.get().getConverterLocator().getConverter(_class).convertToObject(value, Session.get().getLocale());
        return id;
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
        //feedback.setDelay(30000L);
        add(feedback);
        add(tableContainer, editFormContainer);
        tableContainer.add(table = table("table"));
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
        tableContainer.add(new AjaxLink<Void>("btn-new-row") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onNewRow(Optional.ofNullable(target), editForm.getModel());
            }
        });

        if ("edit".equals(getPageParameters().get(0).toOptionalString())) {
            editMode = true;
            modalMode = false;
        }
        editFormContainer.add(new EmptyPanel(MODAL_CONTENT_ID));
        if (editMode) {
            editFormContainer.replace(formInputFragment);
        }
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
                                onEditLinkClick(Optional.of(target), getModel());
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

    public EntityPage<T, ID> doEdit(T entity) {
        this.setEditMode(true).setModelObject(entity);
        return this;
    }

    public void onEditLinkClick(Optional<AjaxRequestTarget> target, IModel<T> model) {
        if (modalMode) {
            modalContainer.replace(formInputFragment);
            target.ifPresent(t -> showModal(t));
        } else {
            RequestCycle.get().setResponsePage(this);
            editFormContainer.replace(formInputFragment);
            editMode = true;
        }
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
        return PersistableDataProvider.of(this::getRepository, this::createSpecification, this::createSort);
    }

    public Specification<T> createSpecification() {
        return null;
    }

    public Sort createSort() {
        return Sort.unsorted();
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
                onEditSubmit(Optional.of(target), form.getModel());
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedback);
            }
        });
        form.add(createInputPanel("input-panel", form.getModel()));
        form.add(new WebMarkupContainer("btn-cancel-modal-edit") {
            @Override
            public boolean isVisible() {
                return modalMode;
            }
        });
        form.add(new Link<Void>("btn-cancel-edit") {
            @Override
            public boolean isVisible() {
                return !modalMode;
            }

            @Override
            public void onClick() {
                RequestCycle.get().setResponsePage(getPage());
                editMode = false;
            }
        });
        return form;
    }

    Form<T> deleteForm(String id) {
        Form<T> deleteForm = new Form<>(id, new CompoundPropertyModel<>(getModel()));
        deleteForm.add(createDetailsPanel("details-panel", deleteForm.getModel()));
        deleteForm.add(new AjaxButton("btn-delete") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (deleteForm.getModel().isPresent().getObject()) {
                    onDelete(Optional.of(target), deleteForm.getModel());
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                EntityPage.this.onError(target, deleteForm.getModel());
            }
        });
        return deleteForm;
    }

    protected WebMarkupContainer createInputPanel(String id, IModel<T> model) {
        return new GenericPanel<>(id, model);
    }

    protected GenericPanel<T> createDetailsPanel(String id, IModel<T> model) {
        return new DetailsPanel(id, model, Stream.of(BeanUtils.getPropertyDescriptors(getEntityClass())).map(FeatureDescriptor::getName).collect(Collectors.toList()), getEntityClass().getSimpleName().toLowerCase());
    }

    public void save(T entity) {
        entity = getRepository().saveAndFlush(entity);
        setModelObject(entity);
    }

    public void delete(T entity) {
        getRepository().delete(entity);
        setModelObject(null);
    }

    public void onNewRow(Optional<AjaxRequestTarget> target, IModel<T> model) {
        EntityPage.this.setModelObject(newInstance());
        onEditLinkClick(target, model);

    }

    public void onEditSubmit(Optional<AjaxRequestTarget> target, IModel<T> model) {
        try {
            save(EntityPage.this.getModelObject());
            success(new StringResourceModel("message.entity.saved", this, EntityPage.this.getModel()).getString());
            if (modalMode) {
                if (target.isPresent()) {
                    AjaxRequestTarget t = target.get();
                    hideModal(t);
                    t.add(table);
                }
            } else {
                RequestCycle.get().setResponsePage(this);
            }
            editMode = editModeOnEditComplete();

        } catch (Exception e) {
            //error(e.getLocalizedMessage());

            error(((NestedRuntimeException) e).getRootCause().getMessage());
        }
        target.ifPresent(
                t -> t.add(feedback)
        );
    }

    public boolean editModeOnEditComplete() {
        return false;
    }

    protected void onError(AjaxRequestTarget target, IModel<T> model) {
        target.add(feedback);
    }

    public void onDelete(Optional<AjaxRequestTarget> target, IModel<T> model) {
        if (model.isPresent().getObject()) {
            T entity = model.getObject();
            delete(entity);
            warn(getString("message.entity.deleted", model));
            target.ifPresent(t -> {
                hideModal(t);
                t.add(table);
                t.add(feedback);
            });
        }
    }

    abstract protected T newInstance();

    protected IColumn<T, String> createPropertyColumn(String pname, boolean enableSort, String resourcePrefix) {
        return new PropertyColumn<>(new ResourceModel(resourcePrefix + pname), pname, pname);
    }

    protected IColumn<T, String> createPropertyColumn(String pname, boolean enableSort, Class propertyClass, String resourcePrefix) {
        return new PropertyColumn<>(new ResourceModel(resourcePrefix + pname), pname, pname);
    }

    protected List<IColumn<T, String>> reflectiveColumns(Class<T> class_, String resourcePrefix, String... properties) {
        List<IColumn<T, String>> list = Lists.newArrayList();
        Stream.of(properties).map(pname -> {
            Class pclass = null;
            try {
                pclass = PropertyResolver.getPropertyClass(pname, class_);
            } catch (Exception e) {
            }
            return createPropertyColumn(pname, true, pclass, resourcePrefix);
        }).forEach(list::add);
        //list.add(new LambdaColumn<Day, String>(new ResourceModel("day.id"), "id", Day::getId));
        return list;
    }

    protected IModel<T> model(T entity) {
        return PersistableModel.of(entity, id -> getRepository().findById(id), this::newInstance);
    }

    protected IModel<T> model(ID pk) {
        return PersistableModel.of(pk, id -> getRepository().findById(id), this::newInstance);
    }

}
