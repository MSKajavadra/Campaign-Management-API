package com.example.Sale.controllers;

import com.example.Sale.models.DTOs.CampaignDTO;
import com.example.Sale.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("campaign-management")
public class CampaignController {
    @Autowired
    private CampaignService campaignService;

    @PostMapping("campaigns")
    public CampaignDTO addCampaign(@RequestBody CampaignDTO campaignDTO){
        return campaignService.addCampaign(campaignDTO);
    }
}
