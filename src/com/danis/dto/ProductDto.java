package com.danis.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ProductDto {
    private int id;
    private String name;
    private String description;
    private int price;
    private int quantity;
}
