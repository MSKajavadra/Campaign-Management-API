package com.example.Sale.repository;

import com.example.Sale.models.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepo extends JpaRepository<PriceHistory,Integer> {
}
