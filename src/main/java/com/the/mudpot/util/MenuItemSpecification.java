package com.the.mudpot.util;

import com.the.mudpot.model.MenuItem;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class MenuItemSpecification {

    public static Specification<MenuItem> filterBy(String location, String severity, String title) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
