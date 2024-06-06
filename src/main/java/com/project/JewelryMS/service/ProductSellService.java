package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.ProductSell.CreateProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellRequest;
import com.project.JewelryMS.model.ProductSell.ProductSellResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductSellService {
    private static final Logger logger = LoggerFactory.getLogger(ProductSellService.class);
    @Autowired
    ProductSellRepository productSellRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PromotionRepository promotionRepository;

    public List<ProductSellResponse> getAllProductSellResponses() {
        List<ProductSellResponse> responses = new ArrayList<>();

        try {
            List<ProductSell> productSells = productSellRepository.findAllWithCategoryAndPromotion();

            for (ProductSell productSell : productSells) {
                ProductSellResponse response = new ProductSellResponse();
                response.setProductID(productSell.getProductID());
                response.setCarat(productSell.getCarat());
                response.setChi(productSell.getChi());
                response.setCost(productSell.getCost());
                response.setDescription(productSell.getPDescription());
                response.setGemstoneType(productSell.getGemstoneType());
                response.setImage(productSell.getImage());
                response.setManufacturer(productSell.getManufacturer());
                response.setMetalType(productSell.getMetalType());
                response.setName(productSell.getPName());
                response.setProductCode(productSell.getProductCode());
                response.setProductCost(productSell.getProductCost());
                response.setStatus(productSell.isPStatus());

                if (productSell.getCategory() != null) {
                    response.setCategory_id(productSell.getCategory().getId());
                }
                int id = (int) productSell.getProductID();
                List<Long> listPromotion = productSellRepository.findPromotionIdsByProductSellId((productSell.getProductID()));
                List<String> promotionIds = new ArrayList<>();

                for (Long promotionId : listPromotion) {
                    promotionIds.add(String.valueOf(promotionId));
                }

                response.setPromotion_id(promotionIds);
                responses.add(response);
            }
        } catch (Exception e) {
            logger.error("An error occurred while retrieving product sell responses: {}", e.getMessage());
            // Optionally, you can re-throw the exception here or handle it differently based on your requirements
        }

        return responses;
    }

    public ProductSellResponse createProductSell(CreateProductSellRequest request) {
        ProductSell productSell = new ProductSell();
        productSell.setCarat(request.getCarat());
        // Set Category
        Optional<Category> categoryOpt = categoryRepository.findById(request.getCategory_id());
        if (categoryOpt.isPresent()) {
            productSell.setCategory(categoryOpt.get());
        } else {
            throw new IllegalArgumentException("Category ID not found");
        }
        productSell.setChi(request.getChi());
        productSell.setCost(request.getCost());
        productSell.setPDescription(request.getPDescription());
        productSell.setGemstoneType(request.getGemstoneType());
        productSell.setImage(request.getImage());
        productSell.setManufacturer(request.getManufacturer());
        productSell.setMetalType(request.getMetalType());
        productSell.setPName(request.getPName());
        productSell.setProductCode(request.getProductCode());
        productSell.setProductCost(request.getProductCost());
        productSell.setPStatus(request.isPStatus());
        // Save ProductSell
        ProductSell productSell1 = productSellRepository.save(productSell);
        return getProductSellById2(productSell1.getProductID());
    }

    // Read all ProductSell entries
    public List<ProductSell> getAllProductSells() {
        return productSellRepository.findAll();
    }

    public ProductSellResponse getProductSellById2(long id) {
        ProductSell productSell = productSellRepository.findByIdWithCategoryAndPromotion(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductSell ID not found"));

        return mapToProductSellResponse(productSell);
    }

    public ProductSell getProductSellById(long id) {
            Optional<ProductSell> productSellOptional = productSellRepository.findById(id);
            if(productSellOptional.isPresent()){
                ProductSell productSell = productSellOptional.get();
                return productSell;
            }
            return productSellOptional.get();
    }



    private ProductSellResponse mapToProductSellResponse(ProductSell productSell) {
        ProductSellResponse response = new ProductSellResponse();
        response.setProductID(productSell.getProductID());
        response.setCarat(productSell.getCarat());
        response.setChi(productSell.getChi());
        response.setCost(productSell.getCost());
        response.setDescription(productSell.getPDescription());
        response.setGemstoneType(productSell.getGemstoneType());
        response.setImage(productSell.getImage());
        response.setManufacturer(productSell.getManufacturer());
        response.setMetalType(productSell.getMetalType());
        response.setName(productSell.getPName());
        response.setProductCode(productSell.getProductCode());
        response.setProductCost(productSell.getProductCost());
        response.setStatus(productSell.isPStatus());

        if (productSell.getCategory() != null) {
            response.setCategory_id(productSell.getCategory().getId());
        }

        List<Long> listPromotion = productSellRepository.findPromotionIdsByProductSellId(productSell.getProductID());
        List<String> promotionIds = listPromotion.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        response.setPromotion_id(promotionIds);

        return response;
    }

    public ProductSellResponse updateProductSell(long id, ProductSellRequest productSellRequest) {
        // Fetch the existing ProductSell entity
        productSellRequest.setProductID(id);
        ProductSell existingProductSell = productSellRepository.findById(productSellRequest.getProductID())
                .orElseThrow(() -> new IllegalArgumentException("ProductSell ID not found"));

        // Update fields from the request
        existingProductSell.setCarat(productSellRequest.getCarat());
        existingProductSell.setChi(productSellRequest.getChi());
        existingProductSell.setCost(productSellRequest.getCost());
        existingProductSell.setPDescription(productSellRequest.getPDescription());
        existingProductSell.setGemstoneType(productSellRequest.getGemstoneType());
        existingProductSell.setImage(productSellRequest.getImage());
        existingProductSell.setManufacturer(productSellRequest.getManufacturer());
        existingProductSell.setMetalType(productSellRequest.getMetalType());
        existingProductSell.setPName(productSellRequest.getPName());
        existingProductSell.setProductCode(productSellRequest.getProductCode());
        existingProductSell.setProductCost(productSellRequest.getProductCost());
        existingProductSell.setPStatus(productSellRequest.isPStatus());

        // Update category
        Category category = categoryRepository.findById(productSellRequest.getCategory_id())
                .orElseThrow(() -> new IllegalArgumentException("Category ID not found"));
        existingProductSell.setCategory(category);

        // Update promotions
        List<Long> promotionIds = productSellRequest.getPromotion_id();
        List<Promotion> promotions = new ArrayList<>();

        for (Long promotionId : promotionIds) {
            Optional<Promotion> promotionOptional = promotionRepository.findById(promotionId);
            promotionOptional.ifPresent(promotions::add);
        }
        existingProductSell.setPromotion(promotions);

        // Save the updated entity
        productSellRepository.save(existingProductSell);

        // Return the updated ProductSellResponse
        return mapToProductSellResponse(existingProductSell);
    }

    public void DeleteProduct(long id){
        Optional<ProductSell> productSellOptional = productSellRepository.findById(id);
        if(productSellOptional.isPresent()){
            ProductSell productSell = productSellOptional.get();
            productSell.setPStatus(false);
            productSellRepository.save(productSell);
        }else {
            throw new RuntimeException("Product Sell with ID " + id + " not found");
        }
    }

}
