package com.example.Sale.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private CampaignDiscountDTO[] campaignDiscountDTOS;
}
