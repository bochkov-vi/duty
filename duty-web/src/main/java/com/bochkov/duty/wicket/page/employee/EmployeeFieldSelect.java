package com.bochkov.duty.wicket.page.employee;

import com.bochkov.duty.jpa.entity.Employee;
import com.bochkov.duty.jpa.repository.EmployeeRepository;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.wicket.StyleAttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.select2.Select2Choice;

import java.util.Collection;
import java.util.Map;

@Getter
@Accessors(chain = true)
public class EmployeeFieldSelect extends Select2Choice<Employee> {

    @SpringBean
    EmployeeRepository repository;

    IModel<Collection<Employee>> excludeEmployeeModel;

    public EmployeeFieldSelect(String id) {
        super(id);
    }

    public EmployeeFieldSelect(String id, IModel<Employee> model) {
        super(id, model);
    }

    public EmployeeFieldSelect(String id, IModel<Employee> model, IModel<Collection<Employee>> excludeEmployeeModel) {
        super(id, model);
        this.excludeEmployeeModel = excludeEmployeeModel;
    }

    @Override
    protected void onInitialize() {
        getSettings().setCloseOnSelect(true);
        getSettings().setTheme("bootstrap4");
        if (excludeEmployeeModel != null) {
            setProvider(new EmployeeDataProvider(excludeEmployeeModel));
        } else {
            setProvider(new EmployeeDataProvider());
        }
        add(new StyleAttributeModifier() {
            @Override
            protected Map<String, String> update(Map<String, String> oldStyles) {
                //oldStyles.put("width", "100%");
                return oldStyles;
            }
        });
        super.onInitialize();
    }
}
