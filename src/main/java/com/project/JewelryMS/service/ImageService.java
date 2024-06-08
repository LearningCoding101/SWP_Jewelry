package com.project.JewelryMS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.JewelryMS.model.Image.ImgbbResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;

@Service
public class ImageService {
    public String imgToBase64String(final RenderedImage img, final String formatName)
    {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try
        {
            ImageIO.write(img, formatName, os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        }
        catch (final IOException ioe)
        {
            throw new UncheckedIOException(ioe);
        }
    }

    public String convertFileToBase64(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        String formatName = getFileExtension(file);
        return imgToBase64String(img, formatName);
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    private final String apiKey = "af69290db25a827d6f9ccd1adb503dbe";

    public String uploadBase64ImageToImgbb(String image) throws IOException {
        String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

        File imageFile = new File(image);

        String base64Image;
        try {
            base64Image = convertFileToBase64(imageFile);
        } catch (IOException e) {
            throw new RuntimeException();
        }

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
                return imageUrl;
            } catch (IOException e) {
                throw new RuntimeException("Fail to Upload Image");
            }
        } else {
            throw new RuntimeException("Fail to Upload Image");
        }
    }

}
