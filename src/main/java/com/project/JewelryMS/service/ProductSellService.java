package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.CreateProductSellRequest;
import com.project.JewelryMS.model.ProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductSellService {
    @Autowired
    ProductSellRepository productSellRepository;

    @Autowired
    CategoryRepository categoryRepository;
    public List<ProductSellResponse> readAllProductSell(){
        return productSellRepository.findAllProductSellResponses();
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
            existingProduct.setCategory(productSellRequest.getCategory());
            existingProduct.setChi(productSellRequest.getChi());
            existingProduct.setCost(productSellRequest.getCost());
            existingProduct.setPDescription(productSellRequest.getPDescription());
            existingProduct.setGemstoneType(productSellRequest.getGemstoneType());
            existingProduct.setManufacturer(productSellRequest.getManufacturer());
            existingProduct.setMetalType(productSellRequest.getMetalType());
            existingProduct.setPName(productSellRequest.getPName());
            existingProduct.setProductCode(productSellRequest.getProductCode());
            existingProduct.setProductCost(productSellRequest.getProductCost());
            existingProduct.setPromotion(productSellRequest.getPromotion());
            existingProduct.setImage(productSellRequest.getImage());
            existingProduct.setPStatus(productSellRequest.isPStatus());
            return productSellRepository.save(existingProduct);
        } else {
            throw new RuntimeException("ProductSell with ID " + productId + " not found");
        }
    }

    public ProductSell createProductSell(CreateProductSellRequest createproductSellRequest) throws IOException {
        ProductSell newProductSell = new ProductSell();
        newProductSell.setCarat(createproductSellRequest.getCarat());
        // Fetch the category from the database
        newProductSell.setCategory(createproductSellRequest.getCategory());
        newProductSell.setChi(createproductSellRequest.getChi());
        newProductSell.setCost(createproductSellRequest.getCost());
        newProductSell.setPDescription(createproductSellRequest.getPDescription());
        newProductSell.setGemstoneType(createproductSellRequest.getGemstoneType());
        newProductSell.setManufacturer(createproductSellRequest.getManufacturer());
        newProductSell.setMetalType(createproductSellRequest.getMetalType());
        newProductSell.setPName(createproductSellRequest.getPName());
        newProductSell.setProductCode(createproductSellRequest.getProductCode());
        newProductSell.setProductCost(createproductSellRequest.getProductCost());
        newProductSell.setPromotion(createproductSellRequest.getPromotion());
        newProductSell.setImage(createproductSellRequest.getImage());
        newProductSell.setPStatus(createproductSellRequest.isPStatus());
        return productSellRepository.save(newProductSell);
    }


    public void deleteProduct(long id){
        productSellRepository.deleteById(id);
    }
}
