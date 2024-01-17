package com.example.Sale.repository;

import com.example.Sale.models.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepo extends JpaRepository<Campaign,Integer> {
    Campaign[] findByStatus(String status);
}
