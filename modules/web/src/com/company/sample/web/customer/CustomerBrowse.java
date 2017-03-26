package com.company.sample.web.customer;

import com.company.sample.entity.Customer;
import com.google.common.base.Strings;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.filter.Clause;
import com.haulmont.cuba.core.global.filter.LogicalCondition;
import com.haulmont.cuba.core.global.filter.LogicalOp;
import com.haulmont.cuba.core.global.filter.QueryFilter;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.GroupDatasource;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class CustomerBrowse extends AbstractLookup {

    @Inject
    private GroupDatasource<Customer, UUID> customersDs;

    @Inject
    private TextField filterField;

    @Override
    public void init(Map<String, Object> params) {
        filterField.addValueChangeListener(e -> {
            String value = (String) e.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                LogicalCondition orCondition = new LogicalCondition("", LogicalOp.OR);
                orCondition.getConditions().add(new Clause("", "e.name like :(?i)custom$paramValue", null, null, null));
                orCondition.getConditions().add(new Clause("", "e.email like :(?i)custom$paramValue", null, null, null));
                QueryFilter queryFilter = new QueryFilter(orCondition);
                customersDs.setQueryFilter(queryFilter);
                customersDs.refresh(ParamsMap.of("paramValue", "%" + value + "%"));
            } else {
                customersDs.setQueryFilter(null);
                customersDs.refresh();
            }
        });

    }

    public void refresh() {
        customersDs.refresh();
    }
}