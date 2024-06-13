package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductBuy;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyRequest;
import com.project.JewelryMS.model.ProductBuy.CreateProductBuyResponse;
import com.project.JewelryMS.model.ProductBuy.ProductBuyResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductBuyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductBuyService {
    @Autowired
    private ProductBuyRepository productBuyRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ApiService apiService;
    @Autowired
    private ImageService imageService;
    public CreateProductBuyResponse createProductBuy(CreateProductBuyRequest request) {
        ProductBuy productBuy = new ProductBuy();
        productBuy.setPbName(request.getName());

        // Find the category by name
        Optional<Category> categoryOpt = categoryRepository.findCategoryByName(request.getCategory());
        if (categoryOpt.isPresent()) {
            productBuy.setCategory(categoryOpt.get());
        } else {
            throw new IllegalArgumentException("Category name not found");
        }

        productBuy.setMetalType(request.getMetalType());
        productBuy.setGemstoneType(request.getGemstoneType());
        String imageUrl = imageService.uploadImageByPathService(request.getImage());
        productBuy.setImage(imageUrl);
        productBuy.setChi(request.getMetalWeight());
        productBuy.setCarat(request.getGemstoneWeight());
        productBuy.setPbCost(calculateProductBuyCost(request.getMetalWeight(), request.getGemstoneWeight(), request.getGemstoneType(), request.getMetalType()));

        ProductBuy savedProductBuy = productBuyRepository.save(productBuy);

        return mapToCreateProductBuyResponse(savedProductBuy);
    }


    private float calculateProductBuyCost(Integer chi, Integer carat, String gemstoneType, String metalType) {
        Float Totalprice = 0.0F;
        Float gemStonePrice = 125000000.0F;
        Float goldPrice = 0.0F;
        //Get API Gold from Info Gold
        goldPrice = Float.parseFloat(apiService.getGoldBuyPricecalculate("http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v"));
        Float TotalGemPrice = (gemStonePrice * carat) * 0.8F;
        Totalprice =  (TotalGemPrice + goldPrice * chi );
        return Totalprice;
    }

    private CreateProductBuyResponse mapToCreateProductBuyResponse(ProductBuy productBuy) {
        CreateProductBuyResponse response = new CreateProductBuyResponse();
        response.setProductBuyID(productBuy.getPK_ProductBuyID());
        response.setCategoryName(productBuy.getCategory().getName());
        response.setPbName(productBuy.getPbName());
        response.setMetalType(productBuy.getMetalType());
        response.setGemstoneType(productBuy.getGemstoneType());
        response.setCost(productBuy.getPbCost());
        response.setImage(productBuy.getImage());
        return response;
    }

    public List<ProductBuyResponse> getAllProductBuys() {
        List<ProductBuy> productBuys = productBuyRepository.findAll();
        return productBuys.stream().map(this::mapToProductBuyResponse).collect(Collectors.toList());
    }

    public ProductBuyResponse getProductBuyById(Long id) {
        Optional<ProductBuy> productBuyOpt = productBuyRepository.findById(id);
        if (productBuyOpt.isPresent()) {
            return mapToProductBuyResponse(productBuyOpt.get());
        } else {
            throw new IllegalArgumentException("ProductBuy ID not found");
        }
    }

    private ProductBuyResponse mapToProductBuyResponse(ProductBuy productBuy) {
        ProductBuyResponse response = new ProductBuyResponse();
        response.setProductBuyID(productBuy.getPK_ProductBuyID());
        response.setCategoryName(productBuy.getCategory().getName());
        response.setPbName(productBuy.getPbName());
        response.setMetalType(productBuy.getMetalType());
        response.setGemstoneType(productBuy.getGemstoneType());
        response.setCost(productBuy.getPbCost());
        return response;
    }


    public String DeleteProductBuy(Long id){
        Optional<ProductBuy> productBuyOptional = productBuyRepository.findById(id);
        if(productBuyOptional.isPresent()){
            ProductBuy productBuy = productBuyOptional.get();
            productBuy.setPbStatus(false);
            productBuyRepository.save(productBuy);
            return "Product Buy" + id + "delete sucessfully!";
        }
        return "Product Buy ID Not Found!!!";
    }

}
