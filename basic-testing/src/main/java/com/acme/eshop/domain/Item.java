package com.acme.eshop.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Item {
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Category category;
}
