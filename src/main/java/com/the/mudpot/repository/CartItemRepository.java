package com.the.mudpot.repository;

import com.the.mudpot.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, String> {}

