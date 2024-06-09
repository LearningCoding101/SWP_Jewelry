
package com.project.JewelryMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.JewelryMS.model.Image.ImgbbResponse;
import com.project.JewelryMS.service.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/images")
@SecurityRequirement(name = "api")
public class ImageController {
    @Autowired
    private ImageService imageService;

    private final String apiKey = "af69290db25a827d6f9ccd1adb503dbe";

    static class FilePathRequest {
        public String file;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/uploadByPath")
    public ResponseEntity<String> uploadImageByPath(@RequestBody FilePathRequest filePathRequest) {
        File imageFile = new File(filePathRequest.file);

        if (!imageFile.exists() || imageFile.isDirectory()) {
            return ResponseEntity.badRequest().body("Invalid image file path");
        }

        String base64Image;
        try {
            base64Image = imageService.convertFileToBase64(imageFile);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to read the file: " + e.getMessage());
        }

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create the body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image", base64Image);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ImgbbResponse imgbbResponse = mapper.readValue(response.getBody(), ImgbbResponse.class);
                String imageUrl = imgbbResponse.getData().getUrl();
                return ResponseEntity.ok(imageUrl);
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Failed to parse Imgbb response: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to upload image");
        }
    }

}
