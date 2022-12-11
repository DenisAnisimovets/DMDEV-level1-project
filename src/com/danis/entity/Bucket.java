package com.danis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Bucket {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
}
