package com.bochkov.duty.wicket.page.planing;

import com.bochkov.duty.jpa.entity.*;
import com.bochkov.duty.jpa.repository.DayRepository;
import com.bochkov.duty.jpa.repository.DutyRepository;
import com.bochkov.duty.jpa.repository.DutyTypeRepository;
import com.bochkov.duty.jpa.repository.PersonRepository;
import com.bochkov.duty.wicket.component.LabelFaIcon;
import com.bochkov.wicket.data.model.PersistableModel;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.GenericWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.springframework.data.domain.Persistable;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

public class DutyCellEditor extends FormComponentPanel<Duty> {
    @Inject
    DutyTypeRepository dutyTypeRepository;
    @Inject
    DayRepository dayRepository;
    @Inject
    PersonRepository personRepository;
    @Inject
    DutyRepository dutyRepository;

    HiddenField<DutyType> dutyTypeField = new HiddenField<DutyType>("dutyType", PersistableModel.of(id -> {
        return dutyTypeRepository.findById(id);
    }), DutyType.class);
    HiddenField<Day> dayField = new HiddenField<>("day", PersistableModel.of(id -> {
        return dayRepository.findById(id);
    }), Day.class);
    HiddenField<Person> personField = new HiddenField<>("person", PersistableModel.of(id -> {
        return personRepository.findById(id);
    }), Person.class);


    public DutyCellEditor(String id, Person person, Day day) {
        super(id);
        setModel(PersistableModel.of(new DutyPK(person, day), dutyRepository::findById));
        dayField.setModelObject(day);
        personField.setModelObject(person);
        dutyTypeField.setModelObject(dutyRepository.findById(new DutyPK(person, day)).map(Duty::getDutyType).orElse(null));
    }

    public DutyCellEditor(String id, IModel<Person> person, IModel<Day> day) {
        this(id, person.getObject(), day.getObject());
    }


    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
        LabelFaIcon dutyTypeLabel = new LabelFaIcon("duty-type-label", dutyTypeField.getModel().map(DutyType::getUiOptions).map(UiOptions::getPlainText), dutyTypeField.getModel().map(DutyType::getUiOptions).map(UiOptions::getFaIcon));
        dutyTypeLabel.setHideOnModelEmpty(true);
        add(dutyTypeLabel);
        dutyTypeLabel.add(new AttributeModifier("duty-type", dutyTypeField.getModel().map(DutyType::getId)));
        dutyTypeLabel.add(new AttributeModifier("person", personField.getModel().map(Persistable::getId)));

       /* faicon.add(new ClassAttributeModifier() {
            @Override
            protected Set<String> update(Set<String> oldClasses) {
                String iconCss = dutyTypeField.getModel().map(DutyType::getUiOptions).map(UiOptions::getFaIcon).getObject();
                if (!Strings.isNullOrEmpty(iconCss))
                    oldClasses.add(iconCss);
                return oldClasses;
            }
        });*/
        add(personField, dayField, dutyTypeField);
        List<DutyType> dutyTypes = personField.getModel().map(Person::getDutyTypes).map(Lists::newArrayList).orElseGet(Lists::newArrayList).getObject();
        add(new ListView<DutyType>("dropdown-item", dutyTypes) {
            @Override
            protected void populateItem(ListItem<DutyType> item) {
                item.add(new AjaxLink<DutyType>("link", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        dutyTypeField.setModelObject(this.getModelObject());
                        target.add(DutyCellEditor.this);
                    }

                }.add(new Label("label", item.getModel().map(DutyType::getName)))
                        .add(new GenericWebMarkupContainer<String>("icon", item.getModel().map(DutyType::getUiOptions).map(UiOptions::getFaIcon).filter(strr -> !Strings.isNullOrEmpty(strr))) {
                            @Override
                            protected void onConfigure() {
                                super.onConfigure();
                                setVisible(getModel().isPresent().getObject());
                            }

                            @Override
                            protected void onInitialize() {
                                super.onInitialize();
                                add(new ClassAttributeModifier() {
                                    @Override
                                    protected Set<String> update(Set<String> oldClasses) {
                                        if (getModel().isPresent().getObject()) {
                                            oldClasses.add("fa");
                                            oldClasses.add(getModelObject());
                                        }
                                        return oldClasses;
                                    }
                                });
                            }
                        }));
            }
        });
        dutyTypeField.add(new AjaxEventBehavior("change") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                target.add(DutyCellEditor.this);
            }
        });
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        /*faicon.setVisible(dutyTypeField.getModel().map(DutyType::getUiOptions).map(UiOptions::getFaIcon).isPresent().getObject());
        text.setVisible(!faicon.isVisible() && text.getDefaultModel().isPresent().getObject());*/
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(OnDomReadyHeaderItem.forScript("initCellEditor()"));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        dutyTypeField.setModelObject(getModel().map(Duty::getDutyType).getObject());
        personField.setModelObject(getModel().map(Duty::getPerson).getObject());
        dayField.setModelObject(getModel().map(Duty::getDay).getObject());


    }

    @Override
    public void convertInput() {
        Duty duty = new Duty();
        duty.setDay(dayField.getModelObject());
        duty.setPerson(personField.getModelObject());
        duty.setDutyType(dutyTypeField.getModelObject());
        setConvertedInput(duty);
    }
}
