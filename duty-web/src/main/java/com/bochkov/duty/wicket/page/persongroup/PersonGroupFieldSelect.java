package com.bochkov.duty.wicket.page.persongroup;

import com.bochkov.duty.jpa.entity.PersonGroup;
import lombok.Getter;
import org.apache.wicket.model.IModel;
import org.wicketstuff.select2.Select2Choice;

@Getter
public class PersonGroupFieldSelect extends Select2Choice<PersonGroup> {

    public PersonGroupFieldSelect(String id) {
        super(id);
    }

    public PersonGroupFieldSelect(String id, IModel<PersonGroup> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setProvider(new PersonGroupChoiceProvider());
        getSettings().setCloseOnSelect(true);
        super.onInitialize();
    }
}
