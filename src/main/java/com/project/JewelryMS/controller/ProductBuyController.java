package com.project.JewelryMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.CalculatePBRequest;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.service.ProductBuyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/productBuy")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ProductBuyController {
    @Autowired
    private ProductBuyService productBuyService;

    @PostMapping("create-ProductBuys")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<Long>> saleCreateBuyOrder(@RequestBody(required = false) byte[] requestData) {
        try {
            String orderJson = new String(requestData); // Convert byte array to String


            System.out.println("Received order: " + orderJson);
            ObjectMapper objectMapper = new ObjectMapper();
            CreateProductBuyRequest[] orderArray = objectMapper.readValue(orderJson, CreateProductBuyRequest[].class);
            List<CreateProductBuyRequest> orderList = Arrays.asList(orderArray);
            List<Long> ProductBuyIDList = new ArrayList<>();
            for(CreateProductBuyRequest createProductBuyRequest: orderList){
                Long Order_ID = productBuyService.createProductBuy(createProductBuyRequest);
                ProductBuyIDList.add(Order_ID);
            }

            // Process orderWrapper as needed
            return ResponseEntity.ok(ProductBuyIDList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
    ////////////////////////Order of Product Buy status 3
    // Get all product buys
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ProductBuyResponse>> getAllProductBuys() {
        List<ProductBuyResponse> productBuys = productBuyService.getAllProductBuys();
        return ResponseEntity.ok(productBuys);
    }


    // Delete a product buy by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<String> deleteProductBuy(@PathVariable Long id) {
        String response = productBuyService.deleteProductBuy(id);
        return ResponseEntity.ok(response);
    }

//    //Update a product buy byID
//    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
//    public ResponseEntity<ProductBuyResponse> updateProductBuy(@PathVariable Long id){
//        ProductBuyResponse productBuyResponse = productBuyService.updateProductBuy(id);
//        return ResponseEntity.ok(productBuyResponse);
//    }
    ////////////////////////Order of Product Buy status 3

    // Get a product buy by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ProductBuyResponse> getProductBuyById(@PathVariable Long id) {
        ProductBuyResponse productBuy = productBuyService.getProductBuyById(id);
        return ResponseEntity.ok(productBuy);
    }

    @PostMapping("/calculate-cost")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<Float> calculateProductBuyCost(@RequestBody CalculatePBRequest createProductBuyRequest) {
        return ResponseEntity.ok(productBuyService.calculateProductBuyCost(createProductBuyRequest));
    }

    // Adjust pricing ratio
    @PostMapping("/adjust-ratio/{ratio}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<Float> adjustRatio(@PathVariable Float ratio) {
        productBuyService.updatePricingRatioPB(ratio);
        return ResponseEntity.ok(ratio);
    }


}
