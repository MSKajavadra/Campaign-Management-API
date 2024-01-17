package com.example.Sale.repository;

import com.example.Sale.models.Campaign;
import com.example.Sale.models.CampaignDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignDiscountRepo extends JpaRepository<CampaignDiscount,Integer> {
    CampaignDiscount[] findByCampaign(Campaign campaign);
}
