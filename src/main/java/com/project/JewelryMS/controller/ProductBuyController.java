package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.service.ProductBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class ProductBuyController {
    @Autowired
    private ProductBuyService productBuyService;

    @GetMapping("productbuy")
    public ResponseEntity<List<ProductBuyResponse>> getAllProductBuys() {
        List<ProductBuyResponse> productBuyDTOs = productBuyService.getAllProductBuys();
        return new ResponseEntity<>(productBuyDTOs, HttpStatus.OK);
    }

    @GetMapping("productbuy/{id}")
    public  ResponseEntity<ProductBuyResponse> getProductBuybyID(@PathVariable Long id){
        return ResponseEntity.ok(productBuyService.getProductBuyById(id));
    }

    @DeleteMapping("productbuy/{id}")
    public ResponseEntity<String> DeleteProductBuybyID(@PathVariable Long id){
        return ResponseEntity.ok(productBuyService.DeleteProductBuy(id));
    }


}
