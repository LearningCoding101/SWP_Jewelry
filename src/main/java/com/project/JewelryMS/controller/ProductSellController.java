package com.project.JewelryMS.controller;


import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.CreateProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellResponse;
import com.project.JewelryMS.service.ImageService;
import com.project.JewelryMS.service.ProductSellService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/productSell")
@SecurityRequirement(name = "api")
public class ProductSellController {

    @Autowired
    ProductSellService productSellService;
    // Create a new ProductSell
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    @PostMapping("/create")
    public ResponseEntity<ProductSellResponse> createProductSell(@ModelAttribute CreateProductSellRequest createProductSellRequest) {
            ProductSellResponse createdProduct = productSellService.createProductSell(createProductSellRequest);
            return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("/readall")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ProductSellResponse>> readAllProductSell(){
        List<ProductSellResponse> allProductSell= productSellService.getAllProductSellResponses();
        System.out.println("Read Product Sell ");
        return ResponseEntity.ok(allProductSell);
    }


    @GetMapping("/read/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ProductSellResponse> getProductSellById(@PathVariable long id) {
        ProductSellResponse response = productSellService.getProductSellById2(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ProductSellResponse> updateProductSell( @PathVariable long id, @RequestBody ProductSellRequest productSellRequest) {
            ProductSellResponse updatedProduct = productSellService.updateProductSell(id,productSellRequest);
            return ResponseEntity.ok(updatedProduct);
    }



    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){

        productSellService.DeleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PostMapping("/adjustRatio/{ratio}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Float> AdjustRatio(@PathVariable Float ratio){
        productSellService.updatePricingRatio(ratio);
        return ResponseEntity.ok(ratio);
    }

    @PostMapping("/addPromotions")
    public ResponseEntity<ProductSell> addPromotionsToProductSell(@RequestBody AddPromotionsRequest request) {
        ProductSell updatedProductSell = productSellService.addPromotionsToProductSell(request);
        return ResponseEntity.ok(updatedProductSell);
    }

}
