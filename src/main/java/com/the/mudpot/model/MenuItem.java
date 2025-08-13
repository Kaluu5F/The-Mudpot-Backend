package com.the.mudpot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "CHAR(36)")
    private String id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private Category category;

    @Size(max = 2000)
    @Column(length = 2000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    // Optional: store currency code (e.g., "LKR", "USD"). Default can be set in service layer.
    @Size(max = 3)
    @Column(length = 3)
    private String currency;

    @Size(max = 512)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private Availability availability;

    // Optional categorization/ops fields
    @Size(max = 64)
    @Column(unique = true, length = 64)
    private String sku;

    // Dietary flags
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
    private boolean lactoseFree;

    // 0-5 spice scale (0 = not spicy)
    @Min(0)
    @Max(5)
    private Integer spicyLevel;

    // Nutrition / prep
    @Min(0)
    private Integer calories;          // kcal
    @Min(0)
    private Integer prepTimeMinutes;   // minutes

    // Free-form labels like "chef-special", "spicy", "kids"
    @ElementCollection
    @CollectionTable(name = "menu_item_tags", joinColumns = @JoinColumn(name = "menu_item_id"))
    @Column(name = "tag", length = 50)
    private Set<String> tags = new HashSet<>();

    // Simple rating aggregation (optional)
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    @Column(precision = 2, scale = 1)
    private BigDecimal averageRating;

    @Min(0)
    private Integer ratingsCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public enum Category {
        APPETIZER, MAIN_COURSE, DESSERT, BEVERAGE, SIDE, SPECIAL
    }

    public enum Availability {
        AVAILABLE, OUT_OF_STOCK, SEASONAL, DISCONTINUED
    }
}
