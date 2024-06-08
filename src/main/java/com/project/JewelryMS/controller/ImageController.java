/*
package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.ProductSell;
import com.project.JewelryMS.model.ProductSell.CreateProductSellRequest;
import com.project.JewelryMS.repository.CategoryRepository;
import com.project.JewelryMS.repository.ProductSellRepository;
import com.project.JewelryMS.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final String apiKey = "bae68497dea95ef8d4911c8d98f34b5c";

    @Autowired
    private ImageService imageService;

    @PostMapping("/uploadByUrl")
    public ResponseEntity<?> uploadImageByUrl(@RequestBody String request) {
        String imageUrl = request;
        if (imageUrl == null || imageUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("Image URL is required");
        }

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageUrl);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Return the response from ImgBB API
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/uploadByFile")
    public ResponseEntity<?> uploadImageByFile(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Image file is required");
        }

        String base64Image = imageService.convertMultipartFileToBase64(file);

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", base64Image);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Return the response from Imgbb API
        return ResponseEntity.ok(response.getBody());
    }

    //Test sections
    @Autowired
    private ProductSellRepository productSellRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody CreateProductSellRequest productSellDTO) {
        // Convert image URL to byte[]

        byte[] imageBytes = Base64.getDecoder().decode(productSellDTO.getImage());

        // Find category by ID
        Category category = categoryRepository.findById(productSellDTO.getCategory_id()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body("Invalid category ID");
        }

        // Create and save product
        ProductSell productSell = new ProductSell();
        productSell.setCarat(productSellDTO.getCarat());
        productSell.setCategory(category);
        productSell.setChi(productSellDTO.getChi());
        productSell.setCost(productSellDTO.getCost());
        productSell.setPDescription(productSellDTO.getPDescription());
        productSell.setGemstoneType(productSellDTO.getGemstoneType());
        productSell.setImage(imageBytes);
        productSell.setManufacturer(productSellDTO.getManufacturer());
        productSell.setMetalType(productSellDTO.getMetalType());
        productSell.setPName(productSellDTO.getPName());
        productSell.setProductCode(productSellDTO.getProductCode());
        productSell.setProductCost(productSellDTO.getProductCost());
        productSell.setPStatus(productSellDTO.isPStatus());

        productSellRepository.save(productSell);

        return ResponseEntity.ok("Product created successfully");
    }

}
*/
