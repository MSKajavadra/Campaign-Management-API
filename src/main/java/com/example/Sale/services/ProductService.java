package com.example.Sale.services;

import com.example.Sale.models.DTOs.PaginationDTO;
import com.example.Sale.models.PriceHistory;
import com.example.Sale.models.Product;
import com.example.Sale.models.DTOs.ProductDTO;
import com.example.Sale.repository.PriceHistoryRepo;
import com.example.Sale.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private PriceHistoryRepo priceHistoryRepo;

    public List<Product> addProducts(List<Product> products) {
        for(Product product:products){
            double mrp= product.getMrp();
            double discount= product.getDiscount();
            double currentPrice=mrp-(mrp*discount/100);
            product.setCurrentPrice(currentPrice);
            productRepo.save(product);

            PriceHistory priceHistory=new PriceHistory();
            priceHistory.setProduct(product);
            priceHistory.setOldPrice(mrp);
            priceHistory.setNewPrice(currentPrice);
            priceHistory.setEffectiveDate(LocalDate.now());
            priceHistoryRepo.save(priceHistory);
        }
        return products;
    }

    public PaginationDTO getProducts(int page, int pageSize) {
        Page<Product> productPage = productRepo.findAll(PageRequest.of(page-1,pageSize));
        int totalPages = productPage.getTotalPages();
        ProductDTO[] productDTOS = new ProductDTO[pageSize];
        int i=0;
        for(Product product:productPage){
            int id = product.getId();
            double mrp = product.getMrp();
            double currentPrice = product.getCurrentPrice();
            double discount = product.getDiscount();
            int inventory = product.getInventory();
            productDTOS[i++] = new ProductDTO(id,mrp,currentPrice,discount,inventory);
        }
        return new PaginationDTO(productDTOS,page,pageSize,totalPages);
    }
}
