package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.entity.Promotion;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductSellService {
    private static final Logger logger = LoggerFactory.getLogger(ProductSellService.class);

    private Float pricingRatio = 1.20F;

    @Autowired
    ProductSellRepository productSellRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ApiService apiService;

    @Autowired
    ImageService imageService;

    public ProductSell removePromotionsFromProductSell(RemovePromotionRequest request) {
        Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSellId());
        if (!productSellOpt.isPresent()) {
            throw new IllegalArgumentException("ProductSell ID not found");
        }

        ProductSell productSell = productSellOpt.get();
        List<Promotion> existingPromotions = productSell.getPromotion();
        for (Long promotionId : request.getPromotionIds()) {
            existingPromotions.removeIf(promotion -> promotion.getPK_promotionID() == promotionId);
        }

        productSell.setPromotion(existingPromotions);
        return productSellRepository.save(productSell);
    }

    public ProductSell addPromotionsToProductSell(AddPromotionsRequest request) {
        Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSellId());
        if (!productSellOpt.isPresent()) {
            throw new IllegalArgumentException("ProductSell ID not found");
        }

        ProductSell productSell = productSellOpt.get();
        List<Promotion> existingPromotions = productSell.getPromotion();
        for (Long promotionId : request.getPromotionIds()) {
            Optional<Promotion> promotionOpt = promotionRepository.findById(promotionId);
            if (promotionOpt.isPresent() && !existingPromotions.contains(promotionOpt.get())) {
                existingPromotions.add(promotionOpt.get());
            }
        }

        productSell.setPromotion(existingPromotions);
        return productSellRepository.save(productSell);
    }

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
                response.setStatus(productSell.isPStatus());

                if (productSell.getCategory() != null) {
                    response.setCategory_id(productSell.getCategory().getId());
                    response.setCategory_name(productSell.getCategory().getName());
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
        productSell.setCost(calculateProductSellCost(request.getChi(),request.getCarat(),request.getGemstoneType(),request.getMetalType(),request.getManufacturer()));
        productSell.setPDescription(request.getPDescription());
        productSell.setPName(request.getPName());
        productSell.setGemstoneType(request.getGemstoneType());
        // Gọi phương thức uploadImageByPath và send MultipartFile file image
        String imageUrl = imageService.uploadImageByPathService(request.getImage());
        productSell.setImage(imageUrl);
        productSell.setManufacturer(request.getManufacturer());
        productSell.setMetalType(request.getMetalType());
        productSell.setProductCode(request.getProductCode());
        productSell.setPStatus(true);
        // Save ProductSell
        ProductSell productSell1 = productSellRepository.save(productSell);
        return getProductSellById2(productSell1.getProductID());
    }

    public Float calculateProductSellCost(Integer chi, Float carat, String gemstoneType, String metalType, Float manufacturer){
        Float Totalprice = 0.0F;
        Float gemStonePrice = 80000000.0F;
        Float goldPrice = 0.0F;
            //Get API Gold from Info Gold
            goldPrice = Float.parseFloat(apiService.getGoldPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v")) / 10.0F;
        Totalprice =  (((gemStonePrice * carat) + (goldPrice * chi) + manufacturer) * GetPricingRatio());
        return Totalprice;
    }

    public Float GetPricingRatio() {
        return pricingRatio;
    }

    public Float updatePricingRatio(Float newRatio) {
        this.pricingRatio = newRatio;
        return pricingRatio;
    }

    // Read all ProductSell entries
    public List<ProductSell> getAllProductSells() {
        return productSellRepository.findAll();
    }

    public ProductSell getProductSellById(long id) {
        Optional<ProductSell> productSellOptional = productSellRepository.findById(id);
        if(productSellOptional.isPresent()){
            ProductSell productSell = productSellOptional.get();
            return productSell;
        }
        return productSellOptional.get();
    }

    public ProductSellResponse getProductSellById2(long id) {
        ProductSell productSell = productSellRepository.findByIdWithCategoryAndPromotion(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductSell ID not found"));

        return mapToProductSellResponse(productSell);
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
        response.setStatus(productSell.isPStatus());

        if (productSell.getCategory() != null) {
            response.setCategory_id(productSell.getCategory().getId());
            response.setCategory_name(productSell.getCategory().getName());
        }

        List<Long> listPromotion = productSellRepository.findPromotionIdsByProductSellId(productSell.getProductID());
        List<String> promotionIds = listPromotion.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        response.setPromotion_id(promotionIds);

        return response;
    }

    public ProductSellResponse updateProductSell(Long id, ProductSellRequest productSellRequest) {
        // Fetch the existing ProductSell entity
        ProductSell existingProductSell = productSellRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductSell ID not found"));

        // Update fields from the request
        existingProductSell.setCarat(productSellRequest.getCarat());
        existingProductSell.setChi(productSellRequest.getChi());
        existingProductSell.setCost(productSellRequest.getCost());
        existingProductSell.setPDescription(productSellRequest.getPDescription());
        existingProductSell.setGemstoneType(productSellRequest.getGemstoneType());
        String imageUrl = imageService.uploadImageByPathService(productSellRequest.getImage());
        existingProductSell.setImage(imageUrl);
        existingProductSell.setManufacturer(productSellRequest.getManufacturer());
        existingProductSell.setMetalType(productSellRequest.getMetalType());
        existingProductSell.setPName(productSellRequest.getPName());
        existingProductSell.setProductCode(productSellRequest.getProductCode());
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

    public void deleteProduct(long id){
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
