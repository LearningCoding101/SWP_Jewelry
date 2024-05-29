package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSellRequest;
import com.project.JewelryMS.repository.ProductSellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductSellService {
    @Autowired
    ProductSellRepository productSellRepository;

    public List<ProductSell> readAllProductSell(){
        return productSellRepository.findAll();
    }

    public ProductSell getProductSellById(long id){
        Optional<ProductSell> product = productSellRepository.findById(id);
        return product.orElse(null);
    }

    public ProductSell updateProductSell(ProductSellRequest productSellRequest) throws IOException {
        long productId = productSellRequest.getProductID();
        Optional<ProductSell> existingProductOpt = productSellRepository.findById(productSellRequest.getProductID());
        if (existingProductOpt.isPresent()) {
            ProductSell existingProduct = existingProductOpt.get();
            // Update fields
            existingProduct.setCarat(productSellRequest.getCarat());
            existingProduct.setCategoryID(productSellRequest.getCategoryID());
            existingProduct.setChi(productSellRequest.getChi());
            existingProduct.setCost(productSellRequest.getCost());
            existingProduct.setPDescription(productSellRequest.getPDescription());
            existingProduct.setGemstoneType(productSellRequest.getGemstoneType());
            existingProduct.setManufacturer(productSellRequest.getManufacturer());
            existingProduct.setMetalType(productSellRequest.getMetalType());
            existingProduct.setPName(productSellRequest.getPName());
            existingProduct.setProductCode(productSellRequest.getProductCode());
            existingProduct.setProductCost(productSellRequest.getProductCost());
            existingProduct.setPromotionID(productSellRequest.getPromotionID());
            existingProduct.setImage(productSellRequest.getImage());
            existingProduct.setPStatus(productSellRequest.isPStatus());
            return productSellRepository.save(existingProduct);
        } else {
            throw new RuntimeException("ProductSell with ID " + productId + " not found");
        }
    }

    public ProductSell createProductSell(ProductSellRequest productSellRequest) throws IOException {
        ProductSell newProductSell = new ProductSell();
        newProductSell.setCarat(productSellRequest.getCarat());
        newProductSell.setCategoryID(productSellRequest.getCategoryID());
        newProductSell.setChi(productSellRequest.getChi());
        newProductSell.setCost(productSellRequest.getCost());
        newProductSell.setPDescription(productSellRequest.getPDescription());
        newProductSell.setGemstoneType(productSellRequest.getGemstoneType());
        newProductSell.setManufacturer(productSellRequest.getManufacturer());
        newProductSell.setMetalType(productSellRequest.getMetalType());
        newProductSell.setPName(productSellRequest.getPName());
        newProductSell.setProductCode(productSellRequest.getProductCode());
        newProductSell.setProductCost(productSellRequest.getProductCost());
        newProductSell.setPromotionID(productSellRequest.getPromotionID());
        newProductSell.setImage(productSellRequest.getImage());
        newProductSell.setPStatus(productSellRequest.isPStatus());
        return productSellRepository.save(newProductSell);
    }


    public void deleteProduct(long id){
        productSellRepository.deleteById(id);
    }
}
