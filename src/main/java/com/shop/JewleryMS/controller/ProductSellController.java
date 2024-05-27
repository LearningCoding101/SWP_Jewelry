package com.shop.JewleryMS.controller;

import com.shop.JewleryMS.entity.ProductSell;
import com.shop.JewleryMS.model.CreateProductSellRequest;
import com.shop.JewleryMS.model.ProductSellRequest;
import com.shop.JewleryMS.service.ProductSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Productsell")
public class ProductSellController {

    @Autowired
    ProductSellService productSellService;

    @GetMapping("/Read")
    public ResponseEntity<List<ProductSell>> ProductSellRead(){
        return ResponseEntity.ok(productSellService.readAllProductSell());
    }

    @PostMapping("/Create")
    public ResponseEntity<ProductSell> ProductSellCreate(@RequestBody CreateProductSellRequest createProductSellRequest){
        return ResponseEntity.ok(productSellService.createProductSell(createProductSellRequest));
    }

    @PostMapping("/Update")
    public ResponseEntity<String> ProductSellUpdate(@RequestBody ProductSellRequest productSellRequest){
        productSellService.updateProductSell(productSellRequest);
        return ResponseEntity.ok("Update Sucessfully !!!");
    }

    @PostMapping("/Delete")
    public ResponseEntity<String> ProductSellDelete(@RequestBody Integer id){
        boolean isDelete = productSellService.deleteCategory(id);
        if(isDelete){
            return ResponseEntity.ok("ProductSell with ID " + id + " was deleted successfully.");
        }else{
            return ResponseEntity.status(404).body("ProductSell with ID " + id + " not found.");
        }
    }


}
