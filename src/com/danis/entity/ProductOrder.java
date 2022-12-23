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
    private int orderId;
    private int productId;
    private int quantity;
    private int price;
    private int sum;
}
