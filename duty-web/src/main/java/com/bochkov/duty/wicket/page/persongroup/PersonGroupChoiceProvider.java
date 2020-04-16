package com.bochkov.duty.wicket.page.persongroup;

import com.bochkov.duty.jpa.entity.PersonGroup;
import com.bochkov.duty.jpa.repository.PersonGroupRepository;
import com.bochkov.wicket.component.select2.data.PersistableChoiceProvider;
import com.google.common.primitives.Ints;
import org.apache.wicket.injection.Injector;

import javax.inject.Inject;
import java.util.Optional;

public class PersonGroupChoiceProvider extends PersistableChoiceProvider<PersonGroup, Integer> {

    @Inject
    PersonGroupRepository repository;

    public PersonGroupChoiceProvider() {
        super("name", "id");
    }

    @Override
    public String idToString(Integer integer) {
        return Optional.ofNullable(integer).map(String::valueOf).orElse(null);
    }

    @Override
    public Integer toId(String str) {
        return Ints.tryParse(str);
    }

    @Override
    public PersonGroupRepository getRepository() {
        if (repository == null) {
            Injector.get().inject(this);
        }
        return repository;
    }
}
