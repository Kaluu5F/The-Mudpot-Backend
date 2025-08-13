package com.the.mudpot.dto;

import com.the.mudpot.model.MenuItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class MenuItemDTO {
    private String id;
    private String name;
    private MenuItem.Category category;
    private String description;
    private BigDecimal price;
    private String currency;
    private String imageUrl;
    private MenuItem.Availability availability;
    private String sku;

    // Dietary flags
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
    private boolean lactoseFree;

    private Integer spicyLevel;
    private Integer calories;
    private Integer prepTimeMinutes;
    private Set<String> tags;

    private BigDecimal averageRating;
    private Integer ratingsCount;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static MenuItemDTO init(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.id = menuItem.getId();
        dto.name = menuItem.getName();
        dto.category = menuItem.getCategory() != null ? menuItem.getCategory() : null;
        dto.description = menuItem.getDescription();
        dto.price = menuItem.getPrice();
        dto.currency = menuItem.getCurrency();
        dto.imageUrl = menuItem.getImageUrl();
        dto.availability = menuItem.getAvailability() != null ? menuItem.getAvailability(): null;
        dto.sku = menuItem.getSku();

        dto.vegetarian = menuItem.isVegetarian();
        dto.vegan = menuItem.isVegan();
        dto.glutenFree = menuItem.isGlutenFree();
        dto.lactoseFree = menuItem.isLactoseFree();

        dto.spicyLevel = menuItem.getSpicyLevel();
        dto.calories = menuItem.getCalories();
        dto.prepTimeMinutes = menuItem.getPrepTimeMinutes();
        dto.tags = menuItem.getTags();

        dto.averageRating = menuItem.getAverageRating();
        dto.ratingsCount = menuItem.getRatingsCount();

        dto.createdAt = menuItem.getCreatedAt();
        dto.updatedAt = menuItem.getUpdatedAt();
        return dto;
    }

    public static List<MenuItemDTO> initList(List<MenuItem> menuItems) {
        return menuItems.stream()
                .map(MenuItemDTO::init)
                .collect(Collectors.toList());
    }
}
