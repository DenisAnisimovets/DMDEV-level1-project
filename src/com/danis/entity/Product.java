package com.danis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Product {
    private int id;
    private String name;
    private String description;
    private int price;
    private int quantity;
}
