package com.danis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductOrder {
    private int order_id;
    private int product_id;
    private int quantity;
    private int price;
    private int sum;
}
