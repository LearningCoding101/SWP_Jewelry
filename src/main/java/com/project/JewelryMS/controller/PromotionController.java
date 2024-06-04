package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.*;
import com.project.JewelryMS.service.PromotionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//import java.sql.Date;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/promotion")
@SecurityRequirement(name = "api")
public class PromotionController {
    @Autowired
    PromotionService promotionService;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    @GetMapping("/productSellIdsByPromotionId")
    public List<PromotionResponse> getProductSellIdsByPromotionId() {
        return promotionService.ReadAllPromotionwithProductID();
    }
    //Create section
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<Promotion> createPromotion(@RequestBody CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = promotionService.createPromotion(createPromotionRequest);
        return ResponseEntity.ok(promotion);
    }

    //Read section
    @GetMapping("/list-all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<Promotion>>readAllPromotion(){
        List<Promotion> promotionList = promotionService.readAllPromotion();
        return ResponseEntity.ok(promotionList);
    }

    @GetMapping("/list-by-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<Promotion> readPromotionById(@RequestParam Long id){
        Promotion promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/list-by-code")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<Promotion>> readPromotionByCode(@RequestParam String id){
        List<Promotion> promotionList = promotionService.getPromotionByCode(id);
        return ResponseEntity.ok(promotionList);
    }

    @GetMapping("/list-active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<Promotion>> readAllActivePromotion() {
        List<Promotion> promotionList = promotionService.readAllActivePromotion();
        return ResponseEntity.ok(promotionList);
    }

    @GetMapping("/list-by-date")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<Promotion>> listPromotionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date targetDate) {
        List<Promotion> promotions = promotionService.getPromotionsByDate(targetDate);
        return ResponseEntity.ok(promotions);
    }

    //Delete section
    @PatchMapping("/delete-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<String> deletePromotion(@RequestBody long id){
        promotionService.deletePromotionById(id);
        return ResponseEntity.ok("Promotion details marked as inactive successfully");
    }

    //Update section
    @PutMapping("/update-promotion-details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<String> updatePromotionDetails(@RequestBody PromotionRequest promotionRequest){
        promotionService.updatePromotionDetails(promotionRequest);
        return ResponseEntity.ok("Promotion Details updated successfully");
    }
}
