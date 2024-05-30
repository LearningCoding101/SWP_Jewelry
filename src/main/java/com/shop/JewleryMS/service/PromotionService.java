package com.shop.JewleryMS.service;

import com.shop.JewleryMS.entity.Promotion;
import com.shop.JewleryMS.model.*;
import com.shop.JewleryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    //Create promotions
    public Promotion createPromotion(CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = new Promotion();

        promotion.setCode(createPromotionRequest.getCode());
        promotion.setDescription(createPromotionRequest.getDescription());
        promotion.setStartDate(createPromotionRequest.getStartDate());
        promotion.setEndDate(createPromotionRequest.getEndDate());

        validateDateOrder(promotion.getStartDate(), promotion.getEndDate());

        // Check if the end date is before the current date
        Date currentDate = new Date();
        if (promotion.getEndDate().before(currentDate)) {
            promotion.setStatus(false);
        } else {
            promotion.setStatus(true);
        }

        return promotionRepository.save(promotion);
    }

    //Reads promotions
    public Promotion getPromotionById(long PK_promotionID) {
        return promotionRepository.findById(PK_promotionID).orElse(null);
    }

    public Promotion getPromotionByCode(String code) {
        return promotionRepository.findByCode(code).orElse(null);
    }
    public List<Promotion> readAllPromotion() {
        List<Promotion> allPromotions = promotionRepository.findAll();
        updatePromotionStatusBasedOnEndDate(allPromotions);
        return allPromotions;
    }

    public List<Promotion> readAllActivePromotion() {
        List<Promotion> activePromotions = promotionRepository.findByStatus(true);
        updatePromotionStatusBasedOnEndDate(activePromotions);
        return activePromotions;
    }

//this method is to avoid reading promotions period that has already ended
        private void updatePromotionStatusBasedOnEndDate(List<Promotion> promotions) {
            Date currentDate = new Date();

            for (Promotion promotion : promotions) {
                if (promotion.getEndDate().before(currentDate)) {
                    // End date has passed, set status to false
                    promotion.setStatus(false);
                    promotionRepository.save(promotion);
                }
            }
        }


    //Update Promotions
    public void updatePromotionDetails(PromotionRequest promotionRequest) {
        Optional<Promotion> promotionUpdate = promotionRepository.findById(promotionRequest.getPK_promotionID());
        if (promotionUpdate.isPresent()) {
            Promotion promotion = promotionUpdate.get();

            validateEndDate(promotion, promotionRequest.getEndDate());
            validateStartDate(promotion, promotionRequest.getStartDate());
            validateDateOrder(promotionRequest.getStartDate(), promotionRequest.getEndDate());
            validateDateDifference(promotionRequest.getStartDate(), promotionRequest.getEndDate());

            promotion.setCode(promotionRequest.getCode());
            promotion.setDescription(promotionRequest.getDescription());
            promotion.setStartDate(promotionRequest.getStartDate());
            promotion.setEndDate(promotionRequest.getEndDate());
            promotionRepository.save(promotion);
        }
    }

        private void validateEndDate(Promotion promotion, Date newEndDate) {
            if (promotion.getEndDate().compareTo(newEndDate) > 0) {
                throw new IllegalArgumentException("End date cannot be earlier " +
                        "than the current end date");
            }
        }
        private void validateStartDate(Promotion promotion, Date newStartDate) {
            Date currentTime = new Date(); // Get the current time

            if (newStartDate.before(currentTime)) {
                throw new IllegalArgumentException("Start date cannot be earlier " +
                        "than the current time");
            }
        }
        private void validateDateOrder(Date startDate, Date endDate) {
            if (endDate.compareTo(startDate) < 0) {
                throw new IllegalArgumentException("End date cannot be before" +
                        " the start date");
            }
        }
        private void validateDateDifference(Date startDate, Date endDate) {
            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diffInDays < 3) {
                throw new IllegalArgumentException("End date should be apart from " +
                        "the start date for at least 3 days");
            }
        }

    //Delete Promotion
    public void deletePromotionById(long id) {
        Optional<Promotion> promotionUpdate = promotionRepository.findById(id);
        promotionUpdate.ifPresent(promotion -> {
            promotion.setStatus(false); // Set status to false
            promotionRepository.save(promotion);
        });
    }
}
