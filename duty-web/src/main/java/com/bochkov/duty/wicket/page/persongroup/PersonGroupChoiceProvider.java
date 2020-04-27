package com.bochkov.duty.wicket.page.persongroup;

import com.bochkov.duty.jpa.entity.EmployeeGroup;
import com.bochkov.duty.jpa.repository.EmployeeGroupRepository;
import com.bochkov.wicket.component.select2.data.PersistableChoiceProvider;
import com.google.common.primitives.Ints;
import org.apache.wicket.injection.Injector;

import javax.inject.Inject;
import java.util.Optional;

public class PersonGroupChoiceProvider extends PersistableChoiceProvider<EmployeeGroup, Integer> {

    @Inject
    EmployeeGroupRepository repository;

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
    public EmployeeGroupRepository getRepository() {
        if (repository == null) {
            Injector.get().inject(this);
        }
        return repository;
    }
}
