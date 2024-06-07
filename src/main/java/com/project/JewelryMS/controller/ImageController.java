package com.project.JewelryMS.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class ImageController {
    private final String apiKey = "bae68497dea95ef8d4911c8d98f34b5c";

    @PostMapping("/fetchImageDetails")
    public ResponseEntity<?> fetchImageDetails(@RequestBody String request) {
        String imageUrl = request;
        if (imageUrl == null || imageUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("Image URL is required");
        }

        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the body
        Map<String, Object> body = new HashMap<>();
        body.put("image", imageUrl);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Return the response from ImgBB API
        return ResponseEntity.ok(response.getBody());
    }


}
