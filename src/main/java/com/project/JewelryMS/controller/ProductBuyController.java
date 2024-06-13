package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.ProductBuy.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyResponse;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.service.ProductBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/productBuy")
public class ProductBuyController {
    @Autowired
    private ProductBuyService productBuyService;

    @PostMapping("/create")
    public ResponseEntity<CreateProductBuyResponse> createProductBuy(@ModelAttribute CreateProductBuyRequest request) {
        CreateProductBuyResponse response = productBuyService.createProductBuy(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("readall")
    public ResponseEntity<List<ProductBuyResponse>> getAllProductBuys() {
        List<ProductBuyResponse> productBuys = productBuyService.getAllProductBuys();
        return ResponseEntity.ok(productBuys);
    }

    @GetMapping("read/{id}")
    public ResponseEntity<ProductBuyResponse> getProductBuyById(@PathVariable Long id) {
        ProductBuyResponse productBuy = productBuyService.getProductBuyById(id);
        return ResponseEntity.ok(productBuy);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteProductBuy(@PathVariable Long id) {
        String response = productBuyService.DeleteProductBuy(id);
        return ResponseEntity.ok(response);
    }


}
