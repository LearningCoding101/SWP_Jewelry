package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.Promotion.CreatePromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionRequest;
import com.project.JewelryMS.model.Promotion.PromotionResponse;
import com.project.JewelryMS.service.PromotionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/promotion")
@SecurityRequirement(name = "api")
public class PromotionController {
    @Autowired
    PromotionService promotionService;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    @GetMapping("/productSellIdsByPromotionId")
    public List<PromotionResponse> getProductSellIdsByPromotionId() {
        return promotionService.ReadAllPromotionWithProductID();
    }
    //Create section
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Promotion> createPromotion(@RequestBody CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = promotionService.createPromotion(createPromotionRequest);
        return ResponseEntity.ok(promotion);
    }

    //Read section
    @GetMapping("/list-all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>>readAllPromotion(){
        List<PromotionResponse> promotionList = promotionService.readAllPromotion();
        return ResponseEntity.ok(promotionList);
    }

    @GetMapping("/list-by-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<PromotionResponse> readPromotionById(@RequestParam Long id){
        PromotionResponse promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/list-by-code")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>> readPromotionByCode(@RequestParam String id){
        List<PromotionResponse> promotionList = promotionService.getPromotionByCode(id);
        return ResponseEntity.ok(promotionList);
    }

    @GetMapping("/list-active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>> readAllActivePromotion() {
        List<PromotionResponse> promotionList = promotionService.readAllActivePromotion();
        return ResponseEntity.ok(promotionList);
    }

    @GetMapping("/list-by-date")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PromotionResponse>> listPromotionsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime targetDate) {
        List<PromotionResponse> promotions = promotionService.getPromotionsByDate(targetDate);
        return ResponseEntity.ok(promotions);
    }

    //Delete section
    @PatchMapping("/delete-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deletePromotion(@RequestBody long id){
        promotionService.deletePromotionById(id);
        return ResponseEntity.ok("Promotion details marked as inactive successfully");
    }

    //Update section
    @PutMapping("/update-promotion-details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updatePromotionDetails(@RequestBody PromotionRequest promotionRequest){
        promotionService.updatePromotionDetails(promotionRequest);
        return ResponseEntity.ok("Promotion Details updated successfully");
    }
}
