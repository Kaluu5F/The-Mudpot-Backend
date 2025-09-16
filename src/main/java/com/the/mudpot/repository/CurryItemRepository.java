package com.the.mudpot.repository;

import com.the.mudpot.model.CurryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurryItemRepository extends JpaRepository<CurryItem, String> {
    boolean existsByNameIgnoreCase(String name);
}
