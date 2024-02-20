package com.example.Sale.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {
    private ProductDTO[] productDTOS;
    private int page;
    private int pageSize;
    private int totalPages;
}
