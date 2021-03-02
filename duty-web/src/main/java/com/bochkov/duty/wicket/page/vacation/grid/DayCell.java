package com.bochkov.duty.wicket.page.vacation.grid;

import com.bochkov.duty.jpa.entity.Day;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.ClassAttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Set;

@Accessors(chain = true)
public class DayCell extends GenericPanel<Day> {

    @Getter
    @Setter
    Boolean selected;

    public DayCell(String id) {
        super(id);
    }

    public DayCell(String id, IModel<Day> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new HiddenField<Boolean>("selected", new PropertyModel<Boolean>(this, "selected")));
        add(new AttributeAppender("data-day", getModel().map(Day::getId)));
        add(new ClassAttributeModifier() {
            @Override
            protected Set<String> update(Set<String> oldClasses) {
                if (selected != null && selected) {
                    oldClasses.add("bg-danger");
                }
                return oldClasses;
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

    }
}
