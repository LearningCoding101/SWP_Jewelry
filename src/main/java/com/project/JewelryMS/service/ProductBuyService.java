package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.repository.ProductBuyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ProductBuyService {
    @Autowired
    private ProductBuyRepository productBuyRepository;

    public List<ProductBuyResponse> getAllProductBuys() {
        List<ProductBuy> productBuys = productBuyRepository.findAll();
        List<ProductBuyResponse> productBuyDTOs = new ArrayList<>();

        for (ProductBuy productBuy : productBuys) {
            ProductBuyResponse productBuyResponse = new ProductBuyResponse();
            productBuyResponse.setBuyID(productBuy.getBuyID());
            productBuyResponse.setProductSellID(productBuy.getOrderDetail().getProductSell().getProductID());
            productBuyResponse.setOrderID(productBuy.getOrderDetail().getPurchaseOrder().getPK_OrderID());
            productBuyResponse.setCategoryID(productBuy.getCategory().getId());
            productBuyResponse.setAppraisalValue(productBuy.getAppraisalValue());
            productBuyResponse.setPcondition(productBuy.getPcondition());
            productBuyResponse.setWeight(productBuy.getWeight());
            productBuyResponse.setDescription(productBuy.getDescription());
            productBuyResponse.setMetalType(productBuy.getMetalType());
            productBuyResponse.setGemstoneType(productBuy.getGemstoneType());
            productBuyResponse.setProductCode(productBuy.getProductCode());
            productBuyResponse.setManufacturer(productBuy.getManufacturer());
            productBuyResponse.setAmount(productBuy.getAmount());
            productBuyResponse.setImage(productBuy.getImage());
            productBuyResponse.setChi(productBuy.getChi());
            productBuyResponse.setCarat(productBuy.getCarat());
            productBuyDTOs.add(productBuyResponse);
        }

        return productBuyDTOs;
    }

    public ProductBuyResponse getProductBuyById(Long buyID) {
        Optional<ProductBuy> productBuyOptional = productBuyRepository.findById(buyID);
        if(productBuyOptional.isPresent()) {
            ProductBuy productBuy = productBuyOptional.get();
            ProductBuyResponse productBuyResponse = new ProductBuyResponse();
            productBuyResponse.setBuyID(productBuy.getBuyID());
            productBuyResponse.setProductSellID(productBuy.getOrderDetail().getProductSell().getProductID());
            productBuyResponse.setOrderID(productBuy.getOrderDetail().getPurchaseOrder().getPK_OrderID());
            productBuyResponse.setCategoryID(productBuy.getCategory().getId());
            productBuyResponse.setAppraisalValue(productBuy.getAppraisalValue());
            productBuyResponse.setPcondition(productBuy.getPcondition());
            productBuyResponse.setWeight(productBuy.getWeight());
            productBuyResponse.setDescription(productBuy.getDescription());
            productBuyResponse.setMetalType(productBuy.getMetalType());
            productBuyResponse.setGemstoneType(productBuy.getGemstoneType());
            productBuyResponse.setProductCode(productBuy.getProductCode());
            productBuyResponse.setManufacturer(productBuy.getManufacturer());
            productBuyResponse.setAmount(productBuy.getAmount());
            productBuyResponse.setImage(productBuy.getImage());
            productBuyResponse.setChi(productBuy.getChi());
            productBuyResponse.setCarat(productBuy.getCarat());
            return productBuyResponse;
        }else{
            throw new RuntimeException("ProductBuy not found with ID: " + buyID);
        }
    }

//    public ProductBuyResponse CreateProductBuy(CreateProductBuyRequest createProductBuyRequest){
//        Optional<>
//    }





    public String DeleteProductBuy(Long id){
        Optional<ProductBuy> productBuyOptional = productBuyRepository.findById(id);
        if(productBuyOptional.isPresent()){
            ProductBuy productBuy = productBuyOptional.get();
            productBuy.setPbstatus(false);
            productBuyRepository.save(productBuy);
            return "Product Buy" + id + "delete sucessfully!";
        }
        return "Product Buy ID Not Found!!!";
    }

}
