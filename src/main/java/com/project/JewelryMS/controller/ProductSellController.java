package com.project.JewelryMS.controller;


import com.project.JewelryMS.model.ProductSell.CreateProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellResponse;
import com.project.JewelryMS.service.ImageService;
import com.project.JewelryMS.service.ProductSellService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
public class ProductSellController {

    @Autowired
    ProductSellService productSellService;
    // Create a new ProductSell
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("productsell/create")
    public ResponseEntity<ProductSellResponse> createProductSell(@RequestBody CreateProductSellRequest createProductSellRequest) {
            ProductSellResponse createdProduct = productSellService.createProductSell(createProductSellRequest);
            return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("productsell/readall")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<ProductSellResponse>> readAllProductSell(){
        List<ProductSellResponse> allProductSell= productSellService.getAllProductSellResponses();
        System.out.println("Read Product Sell ");
        return ResponseEntity.ok(allProductSell);
    }


    @GetMapping("productsell/read/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ProductSellResponse> getProductSellById(@PathVariable long id) {
        ProductSellResponse response = productSellService.getProductSellById2(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("productsell/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ProductSellResponse> updateProductSell( @PathVariable long id, @RequestBody ProductSellRequest productSellRequest) {
            ProductSellResponse updatedProduct = productSellService.updateProductSell(id,productSellRequest);
            return ResponseEntity.ok(updatedProduct);
    }



    @DeleteMapping("productsell/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){

        productSellService.DeleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

}
