package com.bochkov.duty.wicket.component.select2;

import com.google.common.collect.ImmutableList;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

public interface Maskable {


    static Predicate stringMaskExpression(String mask, Expression maskedProperty, CriteriaQuery<?> query, CriteriaBuilder cb) {
        mask = Optional.ofNullable(mask).orElse("%");
        return cb.like(cb.lower(maskedProperty.as(String.class)), Optional.of(mask).map(String::trim).map(String::toLowerCase).map(s -> "%" + s + "%").orElse(null));
    }

    static public <T> Specification<T> maskSpecification(final String mask, final String maskedPopertyName) {
        return (root, query, cb) -> {
            Predicate result;
            Path maskedProperty = fetchNestedPath(root, maskedPopertyName);
            result = stringMaskExpression(mask, maskedProperty, query, cb);
            if (result != null) {
                List<Order> orders;
                if (query.getOrderList() == null) {
                    orders = ImmutableList.of();
                } else {
                    orders = ImmutableList.copyOf(query.getOrderList());
                }
                Expression locate = cb.locate(maskedProperty, mask);
                orders = ImmutableList.<Order>builder().add(cb.asc(locate),  cb.asc(cb.length(maskedProperty)), cb.asc(maskedProperty)).addAll(orders).build();
                query.orderBy(orders);
            }
            return result;
        };
    }

    public static <T> Path<T> fetchNestedPath(Path<T> root, String fieldname) {
        String[] fields = fieldname.split("\\.");
        Path<T> result = null;
        for (String field : fields) {
            if (result == null) {
                result = root.get(field);
            } else {
                result = result.get(field);
            }
        }
        return result;
    }

    static <T> Specification<T> maskSpecification(final String mask, final Iterable<String> maskedPoperties) {
        Specification<T> where = null;
        for (String p : maskedPoperties) {
            Specification<T> spec = maskSpecification(mask, p);
            if (where == null) {
                where = Specification.where(spec);
            } else {
                where = where.or(spec);
            }
        }
        return where;
    }
}
