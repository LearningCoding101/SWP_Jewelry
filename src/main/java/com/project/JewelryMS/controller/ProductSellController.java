package com.project.JewelryMS.controller;


import com.project.JewelryMS.model.ProductSell.CreateProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellResponse;
import com.project.JewelryMS.service.ProductSellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
//@SecurityRequirement(name = "api")
public class ProductSellController {

    @Autowired
    ProductSellService productSellService;
    // Create a new ProductSell
    @PostMapping("productsell")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductSellResponse> createProductSell(@RequestBody CreateProductSellRequest createProductSellRequest) {
            ProductSellResponse createdProduct = productSellService.createProductSell(createProductSellRequest);
            return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("productsell")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductSellResponse>> readAllProductSell(){
        List<ProductSellResponse> allProductSell= productSellService.getAllProductSellResponses();
        System.out.println("Read Product Sell ");
        return ResponseEntity.ok(allProductSell);
    }

    @GetMapping("productsell/{id}")
    public ResponseEntity<ProductSellResponse> getProductSellById(@PathVariable long id) {
        ProductSellResponse response = productSellService.getProductSellById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("productsell/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductSellResponse> updateProductSell( @RequestBody ProductSellRequest productSellRequest) {
            ProductSellResponse updatedProduct = productSellService.updateProductSell(productSellRequest);
            return ResponseEntity.ok(updatedProduct);
    }



    @DeleteMapping("productsell/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@RequestBody int id){

        productSellService.DeleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
