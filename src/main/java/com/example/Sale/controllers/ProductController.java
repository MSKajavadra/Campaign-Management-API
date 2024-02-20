package com.example.Sale.controllers;


import com.example.Sale.models.DTOs.PaginationDTO;
import com.example.Sale.models.Product;
import com.example.Sale.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product-management")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("add-products")
    public List<Product> addProducts(@RequestBody List<Product> products){
        return productService.addProducts(products);
    }
    @GetMapping("products")
    public PaginationDTO getProducts(@RequestParam int page, @RequestParam int pageSize){
        return productService.getProducts(page,pageSize);
    }
}