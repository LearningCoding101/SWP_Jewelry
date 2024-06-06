package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.OrderDetail;
import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.ProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.OrderDetailRepository;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

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

    public ProductBuyResponse CreateProductBuy(CreateProductBuyRequest createProductBuyRequest){
        ProductBuy productBuy = new ProductBuy();
        Optional<Category> categoryOpt = categoryRepository.findById(createProductBuyRequest.getCategoryID());
        Category category = categoryOpt.get();
        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findById(createProductBuyRequest.getOrderDetailID());
        OrderDetail orderDetail = orderDetailOptional.get();
        productBuy.setCategory(category);
        productBuy.setOrderDetail(orderDetail);
        productBuy.setAppraisalValue(createProductBuyRequest.getAppraisalValue());
        productBuy.setPcondition(createProductBuyRequest.getCondition());
        productBuy.setWeight(createProductBuyRequest.getWeight());
        productBuy.setDescription(createProductBuyRequest.getDescription());
        productBuy.setMetalType(createProductBuyRequest.getMetalType());
        productBuy.setGemstoneType(createProductBuyRequest.getGemstoneType());
        productBuy.setProductCode(createProductBuyRequest.getProductCode());
        productBuy.setAmount(createProductBuyRequest.getAmount());
        productBuy.setImage(createProductBuyRequest.getImage());
        productBuy.setChi(createProductBuyRequest.getChi());
        productBuy.setCarat(createProductBuyRequest.getCarat());
        productBuyRepository.save(productBuy);
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
    }

    public ProductBuyResponse updateProductBuy(Long buyID, ProductBuyRequest productBuyRequest) {
        Optional<ProductBuy> productBuyOptional = productBuyRepository.findById(buyID);
        if (productBuyOptional.isPresent()) {
            ProductBuy productBuy = productBuyOptional.get();

            // Update Category
            Optional<Category> categoryOpt = categoryRepository.findById(productBuyRequest.getCategoryID());
            if (categoryOpt.isPresent()) {
                productBuy.setCategory(categoryOpt.get());
            } else {
                throw new RuntimeException("Category not found with id: " + productBuyRequest.getCategoryID());
            }

            // Update OrderDetail
            Optional<OrderDetail> orderDetailOpt = orderDetailRepository.findById(productBuyRequest.getOrderDetailID());
            if (orderDetailOpt.isPresent()) {
                productBuy.setOrderDetail(orderDetailOpt.get());
            } else {
                throw new RuntimeException("OrderDetail not found with id: " + productBuyRequest.getOrderDetailID());
            }

            // Update other fields
            productBuy.setAppraisalValue(productBuyRequest.getAppraisalValue());
            productBuy.setPcondition(productBuyRequest.getCondition());
            productBuy.setWeight(productBuyRequest.getWeight());
            productBuy.setDescription(productBuyRequest.getDescription());
            productBuy.setMetalType(productBuyRequest.getMetalType());
            productBuy.setGemstoneType(productBuyRequest.getGemstoneType());
            productBuy.setProductCode(productBuyRequest.getProductCode());
            productBuy.setManufacturer(productBuyRequest.getManufacturer());
            productBuy.setAmount(productBuyRequest.getAmount());
            productBuy.setImage(productBuyRequest.getImage());
            productBuy.setChi(productBuyRequest.getChi());
            productBuy.setCarat(productBuyRequest.getCarat());

            // Save entity
            productBuyRepository.save(productBuy);

            // Convert and return response
            return convertToProductBuyResponse(productBuy);
        } else {
            throw new RuntimeException("ProductBuy not found with id: " + buyID);
        }
    }

    private ProductBuyResponse convertToProductBuyResponse(ProductBuy productBuy) {
        ProductBuyResponse productBuyResponse = new ProductBuyResponse();
        productBuyResponse.setBuyID(productBuy.getBuyID());
        productBuyResponse.setOrderID(productBuy.getOrderDetail().getPK_ODID());
        productBuyResponse.setProductSellID(productBuy.getOrderDetail().getProductSell().getProductID());
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
    }


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
