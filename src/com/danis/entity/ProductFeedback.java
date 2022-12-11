package com.danis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductFeedback {
    private int id;
    private Timestamp createdAt;
    private int productId;
    private int userId;
    private String feedback;
}
