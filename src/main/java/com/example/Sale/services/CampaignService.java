package com.example.Sale.services;

import com.example.Sale.models.*;
import com.example.Sale.models.DTOs.CampaignDTO;
import com.example.Sale.models.DTOs.CampaignDiscountDTO;
import com.example.Sale.repository.CampaignDiscountRepo;
import com.example.Sale.repository.CampaignRepo;
import com.example.Sale.repository.PriceHistoryRepo;
import com.example.Sale.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CampaignService {
    @Autowired
    private CampaignRepo campaignRepo;
    @Autowired
    private CampaignDiscountRepo campaignDiscountRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private PriceHistoryRepo priceHistoryRepo;

    public CampaignDTO addCampaign(CampaignDTO campaignDTO) {
        LocalDate startDate=campaignDTO.getStartDate();
        LocalDate endDate=campaignDTO.getEndDate();
        String title=campaignDTO.getTitle();
        CampaignDiscountDTO[] campaignDiscountDTOS=campaignDTO.getCampaignDiscountDTOS();

        Campaign campaign=new Campaign();
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setTitle(title);

        LocalDate currentDate=LocalDate.now();
        if(currentDate.isAfter(startDate) && (currentDate.isBefore(endDate) || currentDate.isEqual(endDate))){
            campaign.setStatus("Current");
        }else if(currentDate.isBefore(startDate)){
            campaign.setStatus("Upcoming");
        }else if (currentDate.isAfter(endDate)){
            campaign.setStatus("Past");
        }
        campaignRepo.save(campaign);

        for(CampaignDiscountDTO campaignDiscountDTO:campaignDiscountDTOS){
            Product product=productRepo.findById(campaignDiscountDTO.getProductId()).get();
            double discount=campaignDiscountDTO.getDiscount();

            CampaignDiscount campaignDiscount=new CampaignDiscount();
            campaignDiscount.setCampaign(campaign);
            campaignDiscount.setProduct(product);
            campaignDiscount.setDiscount(discount);

            campaignDiscountRepo.save(campaignDiscount);
        }

        if(campaign.getStatus().equals("Current")){
            startCampaign(campaign);
        }
        return campaignDTO;
    }

    @Scheduled(cron = "*/10 * * * * *")  //"0 0 0 * * ?" for every night 12AM
    public void scheduledMethod() {
        LocalDate currentDate=LocalDate.now();
        Campaign[] campaigns=campaignRepo.findByStatus("Current");
        for(Campaign campaign:campaigns){
            LocalDate endDate=campaign.getEndDate();
            if(currentDate.isAfter(endDate)){
                endCampaign(campaign);
            }
        }
        campaigns=campaignRepo.findByStatus("Upcoming");
        for(Campaign campaign:campaigns){
            LocalDate startDate=campaign.getStartDate();
            LocalDate endDate=campaign.getEndDate();
            if(currentDate.isAfter(startDate) && currentDate.isBefore(endDate)){
                startCampaign(campaign);
            }
        }
    }

    private void startCampaign(Campaign campaign){
        campaign.setStatus("Current");
        CampaignDiscount[] campaignDiscounts=campaignDiscountRepo.findByCampaign(campaign);
        for(CampaignDiscount campaignDiscount:campaignDiscounts){
            Product product=campaignDiscount.getProduct();
            double discount=campaignDiscount.getDiscount();

            double oldCurrentPrice=product.getCurrentPrice();
            double newCurrentPrice=oldCurrentPrice - (oldCurrentPrice*discount/100);
            product.setCurrentPrice(newCurrentPrice);

            double mrp=product.getMrp();
            double newDiscount=100-(newCurrentPrice*100)/mrp;
            product.setDiscount(newDiscount);
            productRepo.save(product);

            PriceHistory priceHistory=new PriceHistory();
            priceHistory.setProduct(product);
            priceHistory.setOldPrice(oldCurrentPrice);
            priceHistory.setNewPrice(newCurrentPrice);
            priceHistory.setEffectiveDate(campaign.getStartDate());
            priceHistoryRepo.save(priceHistory);
        }
        campaignRepo.save(campaign);
    }
    private void endCampaign(Campaign campaign){
        campaign.setStatus("Past");
        CampaignDiscount[] campaignDiscounts=campaignDiscountRepo.findByCampaign(campaign);
        for(CampaignDiscount campaignDiscount:campaignDiscounts){
            Product product=campaignDiscount.getProduct();
            double discount=campaignDiscount.getDiscount();

            double newCurrentPrice=product.getCurrentPrice();
            double oldCurrentPrice=(newCurrentPrice*100) / (100-discount);
            product.setCurrentPrice(oldCurrentPrice);

            double mrp=product.getMrp();
            double oldDiscount=100-(oldCurrentPrice*100)/mrp;
            product.setDiscount(oldDiscount);
            productRepo.save(product);

            PriceHistory priceHistory=new PriceHistory();
            priceHistory.setProduct(product);
            priceHistory.setOldPrice(newCurrentPrice);
            priceHistory.setNewPrice(oldCurrentPrice);
            priceHistory.setEffectiveDate(campaign.getEndDate());
            priceHistoryRepo.save(priceHistory);
        }
        campaignRepo.save(campaign);
    }
}
