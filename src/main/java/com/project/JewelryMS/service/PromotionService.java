package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    // Helper method to format the date
    private String formatDate(LocalDateTime date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH");
        String formattedDate = date.toLocalDate().format(dateFormatter);
        String formattedTime = date.toLocalTime().format(timeFormatter);
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return formattedDate + " " + formattedTime + " (" + dayOfWeek + ")";
    }

    public List<PromotionResponse> ReadAllPromotionwithProductID(){
        List<Promotion> promotionList = promotionRepository.findAllPromotion();
        List<PromotionResponse> responses = new ArrayList<>();
        for(Promotion p : promotionList){
            PromotionResponse promotionResponse = new PromotionResponse();
            promotionResponse.setPromotionID(p.getPK_promotionID());
            promotionResponse.setCode(p.getCode());
            promotionResponse.setDescription(p.getDescription());
            promotionResponse.setStartDate(formatDate(p.getStartDate()));
            promotionResponse.setEndDate(formatDate(p.getEndDate()));
            promotionResponse.setStatus(p.isStatus());
            List<Long> listPromotion = promotionRepository.findProductSellIdsByPromotionId(p.getPK_promotionID());
            List<String> promotionIds = new ArrayList<>();
            for (Long promotionId : listPromotion) {
                promotionIds.add(String.valueOf(promotionId));
            }
            promotionResponse.setProductSell_id(promotionIds);
            responses.add(promotionResponse);
        }
        return responses;
    }


    public Promotion createPromotion(CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = new Promotion();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        LocalDateTime startDate = LocalDateTime.parse(createPromotionRequest.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(createPromotionRequest.getEndDate(), formatter);

        promotion.setCode(createPromotionRequest.getCode());
        promotion.setDescription(createPromotionRequest.getDescription());
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        if(createPromotionRequest.getDiscount()>=0 && createPromotionRequest.getDiscount()<=100) {
            promotion.setDiscount(createPromotionRequest.getDiscount());
            promotionRepository.save(promotion);
        }else{
            throw new RuntimeException("Can't Set Promotion <0 and >100");
        }
        validateDateOrder(promotion.getStartDate(), promotion.getEndDate());

        LocalDateTime currentDate = LocalDateTime.now();
        if (promotion.getEndDate().isBefore(currentDate)) {
            promotion.setStatus(false);
        } else {
            promotion.setStatus(true);
        }

        return promotionRepository.save(promotion);
    }

    public Promotion getPromotionById(long PK_promotionID) {
        return promotionRepository.findById(PK_promotionID).orElse(null);
    }

    public List<Promotion> getPromotionByCode(String code) {
        return promotionRepository.findByCode(code);
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

    public List<Promotion> getPromotionsByDate(LocalDateTime targetDate) {
        List<Promotion> allPromotions = promotionRepository.findAll();
        updatePromotionStatusBasedOnEndDate(allPromotions);

        return allPromotions.stream()
                .filter(promotion -> promotion.getStartDate().isBefore(targetDate)
                        && promotion.getEndDate().isAfter(targetDate))
                .toList();
    }

    private void updatePromotionStatusBasedOnEndDate(List<Promotion> promotions) {
        LocalDateTime currentTime = LocalDateTime.now();

        for (Promotion promotion : promotions) {
            if (promotion.getEndDate().isBefore(currentTime)) {
                promotion.setStatus(false);
                promotionRepository.save(promotion);
            }
        }
    }

    public void updatePromotionDetails(PromotionRequest promotionRequest) {
        Optional<Promotion> promotionUpdate = promotionRepository.findById(promotionRequest.getPK_promotionID());
        if (promotionUpdate.isPresent()) {
            Promotion promotion = promotionUpdate.get();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
            LocalDateTime startDate = LocalDateTime.parse(promotionRequest.getStartDate(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(promotionRequest.getEndDate(), formatter);

            validateEndDate(promotion, endDate);
            validateStartDate(promotion, startDate);
            validateDateOrder(startDate, endDate);
            validateDateDifference(startDate, endDate);

            promotion.setCode(promotionRequest.getCode());
            promotion.setDescription(promotionRequest.getDescription());
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);
            if(promotionRequest.getDiscount()>=0 && promotionRequest.getDiscount()<=100) {
                promotion.setDiscount(promotionRequest.getDiscount());
                promotionRepository.save(promotion);
            }else{
                throw new RuntimeException("Can't Set Promotion <0 and >100");
            }
        }
    }

    private void validateEndDate(Promotion promotion, LocalDateTime newEndDate) {
        if (promotion.getEndDate().compareTo(newEndDate) > 0) {
            throw new IllegalArgumentException("End date cannot be earlier than the current end date");
        }
    }

    private void validateStartDate(Promotion promotion, LocalDateTime newStartDate) {
        LocalDateTime currentTime = LocalDateTime.now();

        if (newStartDate.isBefore(currentTime)) {
            throw new IllegalArgumentException("Start date cannot be earlier than the current time");
        }
    }

    private void validateDateOrder(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.compareTo(startDate) < 0) {
            throw new IllegalArgumentException("End date cannot be before the start date");
        }
    }

    private void validateDateDifference(LocalDateTime startDate, LocalDateTime endDate) {
        long diffInMillies = Math.abs(endDate.getDayOfYear() - startDate.getDayOfYear());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diffInDays < 3) {
            throw new IllegalArgumentException("End date should be apart from the start date for at least 3 days");
        }
    }

    public void deletePromotionById(long id) {
        Optional<Promotion> promotionUpdate = promotionRepository.findById(id);
        promotionUpdate.ifPresent(promotion -> {
            promotion.setStatus(false);
            promotionRepository.save(promotion);
        });
    }
}

