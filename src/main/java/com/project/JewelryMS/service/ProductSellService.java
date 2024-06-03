package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.ProductSellResponse;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductSellService {
    private static final Logger logger = LoggerFactory.getLogger(ProductSellService.class);
    @Autowired
    ProductSellRepository productSellRepository;

    @Autowired
    CategoryRepository categoryRepository;

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

//    public ProductSell getProductSellById(long id){
//        Optional<ProductSell> product = productSellRepository.findById(id);
//        return product.orElse(null);
//    }

//    public ProductSell updateProductSell(ProductSellRequest productSellRequest) throws IOException {
//        long productId = productSellRequest.getProductID();
//        Optional<ProductSell> existingProductOpt = productSellRepository.findById(productSellRequest.getProductID());
//        if (existingProductOpt.isPresent()) {
//            ProductSell existingProduct = existingProductOpt.get();
//            // Update fields
//            existingProduct.setCarat(productSellRequest.getCarat());
//            existingProduct.setCategory(productSellRequest.getCategory());
//            existingProduct.setChi(productSellRequest.getChi());
//            existingProduct.setCost(productSellRequest.getCost());
//            existingProduct.setPDescription(productSellRequest.getPDescription());
//            existingProduct.setGemstoneType(productSellRequest.getGemstoneType());
//            existingProduct.setManufacturer(productSellRequest.getManufacturer());
//            existingProduct.setMetalType(productSellRequest.getMetalType());
//            existingProduct.setPName(productSellRequest.getPName());
//            existingProduct.setProductCode(productSellRequest.getProductCode());
//            existingProduct.setProductCost(productSellRequest.getProductCost());
//            existingProduct.setPromotion(productSellRequest.getPromotion());
//            existingProduct.setImage(productSellRequest.getImage());
//            existingProduct.setPStatus(productSellRequest.isPStatus());
//            return productSellRepository.save(existingProduct);
//        } else {
//            throw new RuntimeException("ProductSell with ID " + productId + " not found");
//        }
//    }

//    public ProductSell createProductSell(CreateProductSellRequest createproductSellRequest) throws IOException {
//        ProductSell newProductSell = new ProductSell();
//        newProductSell.setCarat(createproductSellRequest.getCarat());
//        // Fetch the category from the database
//        newProductSell.setCategory(createproductSellRequest.getCategory());
//        newProductSell.setChi(createproductSellRequest.getChi());
//        newProductSell.setCost(createproductSellRequest.getCost());
//        newProductSell.setPDescription(createproductSellRequest.getPDescription());
//        newProductSell.setGemstoneType(createproductSellRequest.getGemstoneType());
//        newProductSell.setManufacturer(createproductSellRequest.getManufacturer());
//        newProductSell.setMetalType(createproductSellRequest.getMetalType());
//        newProductSell.setPName(createproductSellRequest.getPName());
//        newProductSell.setProductCode(createproductSellRequest.getProductCode());
//        newProductSell.setProductCost(createproductSellRequest.getProductCost());
//        newProductSell.setPromotion(createproductSellRequest.getPromotion());
//        newProductSell.setImage(createproductSellRequest.getImage());
//        newProductSell.setPStatus(createproductSellRequest.isPStatus());
//        return productSellRepository.save(newProductSell);
//    }


//    public void deleteProduct(long id){
//        productSellRepository.deleteById(id);
//    }
}
