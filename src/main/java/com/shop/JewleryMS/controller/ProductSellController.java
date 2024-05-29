package com.shop.JewleryMS.Controller;

import com.shop.JewleryMS.Entity.ProductSell;
import com.shop.JewleryMS.Model.CreateProductSellRequest;
import com.shop.JewleryMS.Model.ProductSellRequest;
import com.shop.JewleryMS.Service.ProductSellService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Productsell")
@SecurityRequirement(name = "api")
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
