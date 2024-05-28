package com.shop.JewleryMS.service;

import com.shop.JewleryMS.entity.Promotion;
import com.shop.JewleryMS.model.*;
import com.shop.JewleryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    public Promotion createPromotion(CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = new Promotion();

        promotion.setCode(createPromotionRequest.getCode());
        promotion.setDescription(createPromotionRequest.getDescription());
        promotion.setStartDate(createPromotionRequest.getStartDate());
        promotion.setEndDate(createPromotionRequest.getEndDate());

        return promotionRepository.save(promotion);
    }

    public Promotion getPromotionById(long PK_promotionID) {
        return promotionRepository.findById(PK_promotionID).orElse(null);
    }

    public List<Promotion> readAllPromotion() {
        return promotionRepository.findAll();
    }

    public void updatePromotionDetails(PromotionRequest promotionRequest) {
        Optional<Promotion> promotionUpdate = promotionRepository.findById(promotionRequest.getPK_promotionID());
        if(promotionUpdate.isPresent()){
            Promotion promotion = promotionUpdate.get();

            // Check if the new end date is later than the current one
            if(promotion.getEndDate().compareTo(promotionRequest.getEndDate()) > 0){
                throw new IllegalArgumentException("End date cannot be earlier " +
                        "than the current end date");
            }

            // Check if the new start date is later than the current one
            if(promotion.getStartDate().compareTo(promotionRequest.getStartDate()) > 0){
                throw new IllegalArgumentException("Start date cannot be earlier " +
                        "than the current start date");
            }

            // Check if the new end date is not before the start date
            if(promotionRequest.getEndDate().compareTo(promotionRequest.getStartDate()) < 0){
                throw new IllegalArgumentException("End date cannot be before " +
                        "the start date");
            }

            // Check if the end date is at least 3 days apart from the start date
            long diffInMillies = Math.abs(promotionRequest.getEndDate().getTime() - promotionRequest.getStartDate().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if(diffInDays < 3){
                throw new IllegalArgumentException("End date should be apart from " +
                        "the start date for at least 3 to 5 days");
            }

            promotion.setCode(promotionRequest.getCode());
            promotion.setDescription(promotionRequest.getDescription());
            promotion.setStartDate(promotionRequest.getStartDate());
            promotion.setEndDate(promotionRequest.getEndDate());
            promotionRepository.save(promotion);
        }
    }

    public void deletePromotionById(long id) {
        promotionRepository.deleteById(id);
    }
}
