package com.bochkov.duty.wicket.page.report;

import com.bochkov.bootstrap.tempusdominus.LocalDateField;
import com.bochkov.duty.jpa.entity.DutyType;
import com.bochkov.duty.jpa.entity.Person;
import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
import com.bochkov.duty.jpa.repository.PersonRepository;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.page.dutytype.DutyTypeFieldSelect;
import com.bochkov.duty.wicket.page.person.PersonFieldSelect;
import com.bochkov.wicket.data.model.PersistableModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IModelComparator;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportInputPanel extends GenericPanel<Report> {
    @SpringBean
    @Getter
    PersonRepository personRepository;

    @SpringBean
    @Getter
    DutyTypeRepository dutyTypeRepository;

    @SpringBean
    @Getter
    ReportRepository reportRepository;

//    SerializableFunction<String, Optional<Person>> personLoader = id -> getPersonRepository().findById(id);
//    SerializableFunction<Integer, Optional<DutyType>> dutyTypeLoader = id -> getDutyTypeRepository().findById(id);

    TextField<Integer> idField;

    TextField<String> titleField;

    FormComponent<DutyType> dutyTypeField;

    FormComponent<Person> chiefField;

    FormComponent<Person> executorField;

    TextField<String> dateTitleField;

    TextField<LocalDate> dateField;

    TextField<LocalDate> dateFromField;

    TextField<LocalDate> dateToField;

    TextField<LocalDateTime> createDateField;

    TextArea<String> genitiveDepartmentField;

    ListView<Person> personsListView;

    Form form = new Form<Void>("form") {
        @Override
        protected void onSubmit() {
            ReportInputPanel.this.onSubmit();
        }
    };

    public ReportInputPanel(String id, IModel<Report> reportModel) {
        super(id, reportModel);
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(form);
        idField = new TextField<>("report.id", Model.of());
        idField.setEnabled(false);
        form.add(idField);

        titleField = new RequiredTextField<>("report.title", Model.of());
        form.add(titleField);

        dutyTypeField = new DutyTypeFieldSelect("report.dutyType", PersistableModel.of(dutyTypeRepository::findById));
        dutyTypeField.setRequired(true);
        form.add(dutyTypeField);

        dateTitleField = new RequiredTextField<>("report.dateTitle", Model.of());
        form.add(dateTitleField);
        dateTitleField.setOutputMarkupId(true);

        dateField = new LocalDateField("report.date", Model.of(), getString("datePattern"));
        dateField.setRequired(true);
        dateField.add(
                new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(dateTitleField);
                        dateTitleField.setModelObject((dateField.getModel().map(date -> date.format(DateTimeFormatter.ofPattern(getString("datePattern")))).getObject()));
                    }
                }
        );
        form.add(dateField);

        dateFromField = new LocalDateField("report.dateFrom", Model.of(), getString("datePattern"));
        dateFromField.setRequired(true);

        form.add(dateFromField);


        dateToField = new LocalDateField("report.dateTo", Model.of(), getString("datePattern"));
        dateToField.setRequired(true);

        form.add(dateToField);

        WebMarkupContainer personsContainer = new WebMarkupContainer("persons");
        personsContainer.setOutputMarkupId(true);
        form.add(personsContainer);
        personsListView = new ListView<Person>("person-list", new ListModel<>()) {
            @Override
            protected void populateItem(ListItem<Person> item) {
                item.add(new Label("person", item.getModel().map(Person::toString)));
                item.add(new AjaxLink<Person>("remove-person", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        List<Person> personList = personsListView.getList();
                        Person person = getModelObject();
                        if (personList != null && person != null) {
                            personList.remove(person);
                            target.add(personsContainer);

                        }
                    }
                });
            }
        };
        personsContainer.add(personsListView);
        personsContainer.add(new PersonFieldSelect("selected-person", PersistableModel.of(personRepository::findById))
                .add(new AjaxFormComponentUpdatingBehavior("select2:select") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        Person person = (Person) getComponent().getDefaultModelObject();
                        List<Person> personList = Lists.newArrayList(personsListView.getList());
                        if (personList != null && person != null && !personList.contains(person)) {
                            personList.add(person);
                            target.add(personsContainer);
                        }
                        getComponent().setDefaultModelObject(null);
                        personsListView.setList(personList);
                    }
                }));


        genitiveDepartmentField = new TextArea<>("report.genitiveDepartment", Model.of());
        genitiveDepartmentField.setRequired(true);
        form.add(genitiveDepartmentField);

        chiefField = new PersonFieldSelect("report.chief", PersistableModel.of(personRepository::findById));
        form.add(chiefField);

        executorField = new PersonFieldSelect("report.executor", PersistableModel.of(personRepository::findById));
        form.add(executorField);

        createDateField = new TextField<>("report.createdDate", Model.of());
        createDateField.setEnabled(false);
        form.add(createDateField);

    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        idField.setModelObject(getModel().map(Report::getId).getObject());
        titleField.setModelObject(getModel().map(Report::getTitle).getObject());
        dutyTypeField.setModelObject(getModel().map(Report::getDutyType).getObject());
        dateTitleField.setModelObject(getModel().map(Report::getTitle).getObject());
        dateField.setModelObject(getModel().map(Report::getDate).getObject());
        dateToField.setModelObject(getModel().map(Report::getDateTo).getObject());
        dateFromField.setModelObject(getModel().map(Report::getDateFrom).getObject());
        chiefField.setModelObject(getModel().map(Report::getChief).getObject());
        executorField.setModelObject(getModel().map(Report::getExecutor).getObject());
        personsListView.setModelObject(getModel().map(Report::getPersons).map(Lists::newArrayList).getObject());
        genitiveDepartmentField.setModelObject(getModel().map(Report::getGenitiveDepartment).getObject());
        createDateField.setModelObject(getModel().map(Report::getCreatedDate).getObject());
    }


    public void onSubmit() {
        Report r = getModelObject();

        r.setDateTitle(dateTitleField.getModelObject());
        r.setTitle(titleField.getModelObject());
        r.setDate(dateField.getModelObject());
        r.setPersons(Sets.newHashSet(personsListView.getList()));
        r.setDateFrom(dateFromField.getModelObject());
        r.setDateTo(dateToField.getModelObject());
        r.setDutyType(dutyTypeField.getModelObject());
        r.setGenitiveDepartment(genitiveDepartmentField.getModelObject());
        r.setChief(chiefField.getModelObject());
        r.setExecutor(executorField.getModelObject());
        this.visitChildren(FormComponent.class, new IVisitor<Component, Object>() {
            @Override
            public void component(Component component, IVisit<Object> iVisit) {
                component.setDefaultModelObject(null);
            }
        });
        personsListView.setList(Lists.newArrayList());

        setModelObject(r);
    }

    @Override
    public IModelComparator getModelComparator() {
        return IModelComparator.ALWAYS_FALSE;
    }
}
