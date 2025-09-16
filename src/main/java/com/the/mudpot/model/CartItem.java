package com.the.mudpot.model;

import com.the.mudpot.model.CurryItem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "cart_items",
        uniqueConstraints = @UniqueConstraint(name = "uk_cart_curry", columnNames = {"cart_id","curry_item_id"}))
public class CartItem {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curry_item_id", nullable = false)
    private CurryItem curryItem;

    @Column(nullable = false)
    private Integer quantity;
}
