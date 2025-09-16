package com.the.mudpot.repository;

import com.the.mudpot.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUserIdAndStatus(String userId, Cart.Status status);
}

