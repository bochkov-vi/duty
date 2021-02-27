package com.bochkov.duty.wicket.page.rang2;

import com.bochkov.duty.jpa.entity.Rang;
import com.bochkov.duty.jpa.repository.RangRepository;
import com.bochkov.wicket.select2.data.PersistableChoiceProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Select2Choice;

public class SelectRang extends Select2Choice<Rang> {

    @SpringBean
    RangRepository repository;

    public SelectRang(String id) {
        super(id);
        setProvider(provider());
    }

    public SelectRang(String id, IModel<Rang> model) {
        super(id, model);
        setProvider(provider());
    }

    @Override
    protected void onInitialize() {
        getSettings().setPlaceholder(getString("rang"))
                .setCloseOnSelect(true)
                .setAllowClear(true)
                .setTheme("bootstrap4");

        super.onInitialize();
    }




    ChoiceProvider<Rang> provider() {
        ChoiceProvider<Rang> provider = PersistableChoiceProvider.of(Rang.class, (s, p) -> repository.findAll(s, p), "name", "id");
        return provider;
    }

}
