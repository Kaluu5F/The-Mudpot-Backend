package com.the.mudpot.service;

import com.the.mudpot.dto.CartDTO;
import com.the.mudpot.dto.CartItemDTO;
import com.the.mudpot.model.Cart;
import com.the.mudpot.model.CartItem;
import com.the.mudpot.model.CurryItem;
import com.the.mudpot.repository.CartItemRepository;
import com.the.mudpot.repository.CartRepository;
import com.the.mudpot.repository.CurryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private  CartItemRepository cartItemRepo;

    @Autowired
    private  CurryItemRepository curryRepo;

    private Cart getOrCreateOpenCart(String userId) {
        return cartRepo.findByUserIdAndStatus(userId, Cart.Status.OPEN)
                .orElseGet(() -> cartRepo.save(Cart.builder()
                        .userId(userId).status(Cart.Status.OPEN).build()));
    }

    @Transactional(readOnly = true)
    @Override
    public CartDTO getMyCart(String userId) {
        Cart cart = getOrCreateOpenCart(userId);
        return toDto(cart);
    }

    @Transactional
    @Override
    public CartDTO addItem(String userId, String curryItemId, int qty) {
        if (qty <= 0) qty = 1;
        Cart cart = getOrCreateOpenCart(userId);
        var product = curryRepo.findById(curryItemId)
                .orElseThrow(() -> new IllegalArgumentException("Curry item not found"));

        // guard availability
        var st = product.getAvailability();
        if (st == CurryItem.Availability.DISCONTINUED || st == CurryItem.Availability.OUT_OF_STOCK) {
            throw new IllegalStateException("Item unavailable: " + product.getName());
        }

        var existing = cart.getItems().stream()
                .filter(i -> i.getCurryItem().getId().equals(curryItemId))
                .findFirst().orElse(null);

        if (existing == null) {
            cart.getItems().add(CartItem.builder()
                    .cart(cart).curryItem(product).quantity(qty).build());
        } else {
            existing.setQuantity(existing.getQuantity() + qty);
        }
        return toDto(cartRepo.save(cart));
    }

    @Transactional
    @Override
    public CartDTO setQuantity(String userId, String curryItemId, int qty) {
        if (qty < 0) qty = 0;
        Cart cart = getOrCreateOpenCart(userId);
        var item = cart.getItems().stream()
                .filter(i -> i.getCurryItem().getId().equals(curryItemId))
                .findFirst().orElse(null);
        if (item == null) return toDto(cart);
        if (qty == 0) {
            cart.getItems().remove(item);
            cartItemRepo.delete(item);
        } else {
            item.setQuantity(qty);
        }
        return toDto(cartRepo.save(cart));
    }

    @Transactional
    @Override
    public CartDTO removeItem(String userId, String curryItemId) {
        return setQuantity(userId, curryItemId, 0);
    }

    @Transactional
    @Override
    public void clear(String userId) {
        Cart cart = getOrCreateOpenCart(userId);
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    private CartDTO toDto(Cart cart) {
        var dto = new CartDTO();
        dto.setId(cart.getId());
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem ci : cart.getItems()) {
            CurryItem p = ci.getCurryItem();
            BigDecimal unit = p.getPrice() == null ? BigDecimal.ZERO : p.getPrice().setScale(2, RoundingMode.HALF_UP);
            BigDecimal line = unit.multiply(BigDecimal.valueOf(ci.getQuantity())).setScale(2, RoundingMode.HALF_UP);
            subtotal = subtotal.add(line);

            var it = new CartItemDTO();
            it.setCurryItemId(p.getId());
            it.setQuantity(ci.getQuantity());
            it.setName(p.getName());
            it.setPrice(unit);
            it.setImageUrl(p.getImageUrl());
            it.setAvailability(p.getAvailability().name());
            it.setLineTotal(line);
            dto.getItems().add(it);
        }
        dto.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        return dto;
    }
}

