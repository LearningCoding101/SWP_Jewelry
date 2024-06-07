package com.project.JewelryMS.controller;

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

@RestController
@RequestMapping("/images") //Temporarily remove /api for easier testing
public class ImageController {
    private final String apiKey = "bae68497dea95ef8d4911c8d98f34b5c";

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

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Return the response from Imgbb API
        return ResponseEntity.ok(response.getBody());
    }
}
