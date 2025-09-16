package com.the.mudpot.dto;

import com.the.mudpot.model.CurryItem;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class CurryItemDTO {
    private String id;

    @NotBlank
    public String name;

    @NotNull @DecimalMin("0.00")
    public BigDecimal price;

    @Size(max = 2000)
    public String description;

    @NotNull
    public CurryItem.Availability availability;

    // Returned to client after store
    public String imageUrl;

    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;

    // getters & setters …

    public static CurryItemDTO fromEntity(CurryItem e) {
        CurryItemDTO d = new CurryItemDTO();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setPrice(e.getPrice());
        d.setDescription(e.getDescription());
        d.setAvailability(e.getAvailability());
        d.setImageUrl(e.getImageUrl());
        d.setCreatedAt(e.getCreatedAt());
        d.setUpdatedAt(e.getUpdatedAt());
        return d;
    }

    public CurryItem toEntity() {
        return CurryItem.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .availability(this.availability)
                .build();
    }

}
