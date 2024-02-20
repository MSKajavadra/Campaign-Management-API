package com.example.Sale.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private int Id;
    private double mrp;
    private double currentPrice;
    private double discount;
    private int inventory;
}
