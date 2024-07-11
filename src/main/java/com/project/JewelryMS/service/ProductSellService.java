package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.*;
import com.project.JewelryMS.model.ProductSell.*;
import com.project.JewelryMS.repository.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductSellService {
    private static final Logger logger = LoggerFactory.getLogger(ProductSellService.class);

    @Autowired
    PricingRatioRepository pricingRatioRepository;

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

    @Autowired
    ProductSellPromotionRepository productSellPromotionRepository;

    public ProductSell removePromotionsFromProductSell(RemovePromotionRequest request) {
        Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSellId());
        if (!productSellOpt.isPresent()) {
            throw new IllegalArgumentException("ProductSell ID not found");
        }

        ProductSell productSell = productSellOpt.get();
        List<ProductSell_Promotion> existingPromotions = productSellPromotionRepository.findByProductSell(productSell);

        for (Long promotionId : request.getPromotionIds()) {
            existingPromotions.removeIf(promotion -> promotion.getPromotion().getPK_promotionID() == promotionId);
        }

        productSell.setProductSellPromotions(existingPromotions);
        return productSellRepository.save(productSell);
    }

    public ProductSell addPromotionsToProductSell(AddPromotionsRequest request) {
        Optional<ProductSell> productSellOpt = productSellRepository.findById(request.getProductSellId());
        if (!productSellOpt.isPresent()) {
            throw new IllegalArgumentException("ProductSell ID not found");
        }

        ProductSell productSell = productSellOpt.get();
        List<ProductSell_Promotion> existingPromotions = productSellPromotionRepository.findByProductSell(productSell);

        for (Long promotionId : request.getPromotionIds()) {
            Optional<Promotion> promotionOpt = promotionRepository.findById(promotionId);
            if (promotionOpt.isPresent() && existingPromotions.stream().noneMatch(p -> p.getPromotion().equals(promotionOpt.get()))) {
                ProductSell_Promotion newProductSellPromotion = new ProductSell_Promotion();
                newProductSellPromotion.setProductSell(productSell);
                newProductSellPromotion.setPromotion(promotionOpt.get());
                productSellPromotionRepository.save(newProductSellPromotion);
                existingPromotions.add(newProductSellPromotion);
            }
        }

        productSell.setProductSellPromotions(existingPromotions);
        return productSellRepository.save(productSell);
    }

    public List<ProductSellResponse> getAllProductSellResponses() {
        List<ProductSellResponse> responses = new ArrayList<>();

        List<ProductSell> productSells = productSellRepository.findAllWithCategoryAndPromotion();

        Map<Long, List<Long>> productPromotionMap = new HashMap<>();
        List<Object[]> promotionData = productSellRepository.findAllPromotionIds();
        for (Object[] entry : promotionData) {
            Long productId = (Long) entry[0];
            Long promotionId = (Long) entry[1];
            productPromotionMap.computeIfAbsent(productId, k -> new ArrayList<>()).add(promotionId);
        }

        for (ProductSell productSell : productSells) {
            ProductSellResponse response = mapProductSellToResponse(productSell);

            List<Long> promotionIds = productPromotionMap.getOrDefault(productSell.getProductID(), new ArrayList<>());
            List<String> promotionIdStrings = promotionIds.stream().map(String::valueOf).collect(Collectors.toList());
            response.setPromotion_id(promotionIdStrings);

            responses.add(response);
        }
        return responses;
    }

    private ProductSellResponse mapProductSellToResponse(ProductSell productSell) {
        ProductSellResponse response = new ProductSellResponse();

        response.setProductID(productSell.getProductID());
        if (productSell.getCarat() != null) {
            response.setCarat(productSell.getCarat());
        }
        if (productSell.getChi() != null) {
            response.setChi(productSell.getChi());
        }
        response.setCost(productSell.getCost());
        response.setPDescription(productSell.getPDescription());
        if(productSell.getGemstoneType() !=null) {
            response.setGemstoneType(productSell.getGemstoneType());
        }
        response.setImage(productSell.getImage());
        response.setManufacturer(productSell.getManufacturer());
        response.setManufactureCost(productSell.getManufactureCost());
        if (productSell.getMetalType() != null){
            response.setMetalType(productSell.getMetalType());
        }
        response.setPName(productSell.getPName());
        response.setProductCode(productSell.getProductCode());
        response.setStatus(productSell.isPStatus());

        if (productSell.getCategory() != null) {
            response.setCategory_id(productSell.getCategory().getId());
            response.setCategory_name(productSell.getCategory().getName());
        }

        List<String> promotionIds = productSellRepository.findPromotionIdsByProductSellId(productSell.getProductID())
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        response.setPromotion_id(promotionIds);

        return response;
    }



    public ProductSellResponse createProductSell(CreateProductSellRequest request) {
        ProductSell productSell = new ProductSell();
        if (request.getCarat() != null) {
            productSell.setCarat(request.getCarat());
        }
        // Set Category
        Optional<Category> categoryOpt = categoryRepository.findById(request.getCategory_id());
        if (categoryOpt.isPresent()) {
            productSell.setCategory(categoryOpt.get());
        } else {
            throw new IllegalArgumentException("Category ID not found");
        }
        if (request.getChi() != null) {
            productSell.setChi(request.getChi());
        }
        productSell.setCost(calculateProductSellCost(request.getChi(),request.getCarat(),request.getGemstoneType(),request.getMetalType(),request.getManufactureCost()));
        productSell.setPDescription(request.getPdescription());
        productSell.setPName(request.getPname());
        if(request.getGemstoneType() != null) {
            productSell.setGemstoneType(request.getGemstoneType());
        }
        // Gọi phương thức uploadImageByPath và send MultipartFile file image
        String imageUrl = imageService.uploadImageByPathService(request.getImage());
        productSell.setImage(imageUrl);
        productSell.setManufacturer(request.getManufacturer());
        productSell.setManufactureCost(request.getManufactureCost());
        if(request.getMetalType() != null) {
            productSell.setMetalType(request.getMetalType());
        }
        productSell.setProductCode(request.getProductCode());
        productSell.setPStatus(true);
        // Save ProductSell
        ProductSell productSell1 = productSellRepository.save(productSell);
        return getProductSellById2(productSell1.getProductID());
    }
    private Float goldPrice;

    @PostConstruct
    private void initializeGoldPrice() {
        this.goldPrice = Float.parseFloat(apiService.getGoldBuyPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v"));
    }

    public Float calculateProductSellCost(Integer chi, Float carat, String gemstoneType, String metalType, Float manufacturerCost){
        Float totalGemPrice = 0.0F;
        Float totalPrice = 0.0F;

        if (gemstoneType != null && carat != null) {
            Float gemStonePrice = 100000000.0F; // Price per carat
            totalGemPrice = (gemStonePrice * carat);
        }

        Float totalGoldPrice = 0.0F;
        if (metalType != null && chi != null) {
            Float goldPrices = goldPrice;
            totalGoldPrice = goldPrices  * chi;
        }

        totalPrice = (totalGemPrice + totalGoldPrice + manufacturerCost) * getPricingRatioPS();

        return totalPrice / 1000.0F;
    }

    public Float getPricingRatioPS(){
        Optional<PricingRatio> pricingRatioOptional = pricingRatioRepository.findById(1L);
        if(pricingRatioOptional.isPresent()){
            PricingRatio pricingRatio1 = pricingRatioOptional.get();
            return pricingRatio1.getPricingRatioPS();
        }
        return 0.0F;
    }

    public Float updatePricingRatioPS(Float newRatio) {
        Optional<PricingRatio> pricingRatioOptional = pricingRatioRepository.findById(1L);
        if(pricingRatioOptional.isPresent()) {
            PricingRatio pricingRatio = pricingRatioOptional.get();
            pricingRatio.setPricingRatioPS(newRatio);
            pricingRatioRepository.save(pricingRatio);
        }
        return newRatio;
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
        }else{
            return new ProductSell();
        }
    }

    public ProductSellResponse getProductSellById2(long id) {
        ProductSell productSell = productSellRepository.findByIdWithCategoryAndPromotion(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductSell ID not found"));

        return mapProductSellToResponse(productSell);
    }


    public ProductSellResponse updateProductSell(Long id, ProductSellRequest productSellRequest) {
        // Fetch the existing ProductSell entity
        ProductSell existingProductSell = productSellRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductSell ID not found"));

        // Update fields from the request
        existingProductSell.setCarat(productSellRequest.getCarat());
        existingProductSell.setChi(productSellRequest.getChi());
        existingProductSell.setCost(productSellRequest.getCost());
        existingProductSell.setPDescription(productSellRequest.getPdescription());
        existingProductSell.setGemstoneType(productSellRequest.getGemstoneType());
        String imageUrl = imageService.uploadImageByPathService(productSellRequest.getImage());
        existingProductSell.setImage(imageUrl);
        existingProductSell.setManufacturer(productSellRequest.getManufacturer());
        existingProductSell.setManufactureCost(productSellRequest.getManufactureCost());
        existingProductSell.setMetalType(productSellRequest.getMetalType());
        existingProductSell.setPName(productSellRequest.getPname());
        existingProductSell.setProductCode(productSellRequest.getProductCode());
        // Update category
        Category category = categoryRepository.findById(productSellRequest.getCategory_id())
                .orElseThrow(() -> new IllegalArgumentException("Category ID not found"));
        existingProductSell.setCategory(category);


        // Save the updated entity
        productSellRepository.save(existingProductSell);

        // Return the updated ProductSellResponse
        return mapProductSellToResponse(existingProductSell);
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

    public ProductSellResponse findByProductCode(String productCode) {
        ProductSell productSell = productSellRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with code: " + productCode));

        return mapProductSellToResponse(productSell);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

}
