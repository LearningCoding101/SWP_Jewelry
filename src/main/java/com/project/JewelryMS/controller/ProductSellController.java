package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.CreateProductSellRequest;
import com.project.JewelryMS.model.ProductSellRequest;
import com.project.JewelryMS.service.ProductSellService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("productsell")
@SecurityRequirement(name = "api")
public class ProductSellController {

    @Autowired
    ProductSellService productSellService;
    // Create a new ProductSell
    @PostMapping("create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductSell> createProductSell(@RequestBody CreateProductSellRequest createProductSellRequest) {
        try {
            ProductSell createdProduct = productSellService.createProductSell(createProductSellRequest);
            return ResponseEntity.ok(createdProduct);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductSell>> readAllProductSell(){
        return ResponseEntity.ok(productSellService.readAllProductSell());
    }

    @PostMapping("update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductSell> updateProductSell( @RequestBody ProductSellRequest productSellRequest) {
        try {
            ProductSell updatedProduct = productSellService.updateProductSell(productSellRequest);
            return ResponseEntity.ok(updatedProduct);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get a specific ProductSell by ID
    @GetMapping("/readbyid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductSell> getProductSell(@RequestParam("id") int id) {
        ProductSell productSell = productSellService.getProductSellById(id);
        if (productSell != null) {
            return ResponseEntity.ok(productSell);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@RequestBody int id){
        productSellService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
