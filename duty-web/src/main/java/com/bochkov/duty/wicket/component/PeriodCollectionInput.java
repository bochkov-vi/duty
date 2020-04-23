package com.bochkov.duty.wicket.component;

import com.bochkov.duty.jpa.entity.Period;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.form.datetime.LocalTimeTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PeriodCollectionInput extends FormComponentPanel<Collection<Period>> {

    IModel<List<Period>> innerModel = new ListModel<>(Lists.newArrayList());

    ListView<Period> listView;

    public PeriodCollectionInput(String id) {
        super(id);
    }

    public PeriodCollectionInput(String id, IModel<Collection<Period>> model) {
        super(id, model);

    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        innerModel.setObject(Optional.ofNullable(getModel().getObject()).map(Lists::newArrayList).orElseGet(Lists::newArrayList));
        WebMarkupContainer container = new WebMarkupContainer("container");
        add(container);
        container.setOutputMarkupId(true);
        Form<Void> form = new Form<>("form");
        container.add(form);
        IModel<LocalTime> start = Model.of();
        IModel<Duration> duration = Model.of();

        form.add(new LocalTimeTextField("start", start, "HH:mm"));
        form.add(new DurationTextField("duration", duration));
        form.add(new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                target.add(container);
                innerModel.getObject().add(new Period(start.getObject(), duration.getObject()));
            }
        });
        RefreshingView<Period> listView = new RefreshingView<Period>("row", innerModel) {
            @Override
            protected Iterator<IModel<Period>> getItemModels() {
                return innerModel.map(c -> c.stream().map(p -> (IModel<Period>) Model.of(p)).iterator()).getObject();
            }

            @Override
            protected void populateItem(Item<Period> item) {
                item.add(new Label("start", item.getModel().map(Period::getStart).map(lt -> lt.format(DateTimeFormatter.ofPattern(getString("timePattern"))))));
                item.add(new Label("duration", item.getModel().map(Period::getDuration).map(Duration::toMillis).map(m -> DurationFormatUtils.formatDuration(m, getString("timePattern")))));
                item.add(new AjaxLink<Period>("delete", item.getModel()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        target.add(container);
                        innerModel.getObject().remove(getModelObject());
                    }
                });
            }
        };
        container.add(listView);
        add(new IValidator<Collection<Period>>() {
            @Override
            public void validate(IValidatable<Collection<Period>> validatable) {
                if (validatable != null) {
                    Set<Period> set = Sets.newTreeSet();
                    boolean duplicates = validatable.getValue().stream().anyMatch(period -> !set.add(period));
                    if (duplicates) {
                        IValidationError error = new ValidationError("Периоды пересекаются");
                        validatable.error(error);
                    }
                }
            }
        });
    }

    @Override
    protected void onBeforeRender() {
        innerModel.setObject(getModel().map(Lists::newArrayList).orElseGet(Lists::newArrayList).getObject());
        super.onBeforeRender();
    }

    @Override
    public void convertInput() {
        setConvertedInput(innerModel.map(Sets::newTreeSet).getObject());
    }
}
