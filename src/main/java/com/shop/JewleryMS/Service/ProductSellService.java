package com.shop.JewleryMS.Service;

import com.shop.JewleryMS.Entity.ProductSell;
import com.shop.JewleryMS.Model.CreateProductSellRequest;
import com.shop.JewleryMS.Model.ProductSellRequest;
import com.shop.JewleryMS.repository.ProductSellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSellService {

    @Autowired
    ProductSellRepository productSellRepository;

    public List<ProductSell> readAllProductSell(){
        List<ProductSell> productSellList = productSellRepository.findAll();
        return productSellList;
    }

    public ProductSell createProductSell(CreateProductSellRequest createProductSellRequest){
        ProductSell ps = new ProductSell();
        ps.setName(createProductSellRequest.getName());
        ps.setCost(createProductSellRequest.getCost());
        ps.setStatus(createProductSellRequest.getStatus());
        ps.setDescription(createProductSellRequest.getDescription());
        ps.setMetalType(createProductSellRequest.getMetalType());
        ps.setGemstoneType(createProductSellRequest.getGemstoneType());
        ps.setProductCode(createProductSellRequest.getProductCode());
        ps.setManufacturer(createProductSellRequest.getManufacturer());
        ps.setProductCost(createProductSellRequest.getProductCost());
        ps.setImage(createProductSellRequest.getImage());
        ps.setChi(createProductSellRequest.getChi());
        ps.setCarat(createProductSellRequest.getCarat());
        return productSellRepository.save(ps);
    }

    public void updateProductSell(ProductSellRequest productSellRequest) {
        Optional<ProductSell> optionalProductSell = productSellRepository.findById(productSellRequest.getProductID());
        if (optionalProductSell.isPresent()) {
            ProductSell productSell = optionalProductSell.get();
            productSell.setCategoryID(productSellRequest.getCategoryID());
            productSell.setPromotionID(productSellRequest.getPromotionID());
            productSell.setName(productSellRequest.getName());
            productSell.setCost(productSellRequest.getCost());
            productSell.setStatus(productSellRequest.getStatus());
            productSell.setDescription(productSellRequest.getDescription());
            productSell.setMetalType(productSellRequest.getMetalType());
            productSell.setGemstoneType(productSellRequest.getGemstoneType());
            productSell.setProductCode(productSellRequest.getProductCode());
            productSell.setManufacturer(productSellRequest.getManufacturer());
            productSell.setProductCost(productSellRequest.getProductCost());
            productSell.setImage(productSellRequest.getImage());
            productSell.setChi(productSellRequest.getChi());
            productSell.setCarat(productSellRequest.getCarat());
            productSellRepository.save(productSell);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    public boolean deleteCategory(Integer id) {
        Optional<ProductSell> optionalProductSell = productSellRepository.findById(id);
        if (optionalProductSell.isPresent()) {
            productSellRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

