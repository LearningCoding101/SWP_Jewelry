package com.project.JewelryMS.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper jsonMapper;

    public ApiService(RestTemplate restTemplate,
                      ObjectMapper jsonMapper) {
        this.restTemplate = restTemplate;
        this.jsonMapper = jsonMapper;
    }

    public String getGoldPrice(String url) {
        String xmlResponse = restTemplate.getForObject(url, String.class);

        String[] replacements = {"Tên giá vàng",
                "Hàm lượng kara",
                "Hàm lượng vàng",
                "Giá mua vào",
                "Giá mua ra",
                "Giá bán thế giới",
                "Thời gian nhập giá vàng"};
        int number = 1;

        Pattern pattern = Pattern.compile("\"@(n|k|h|pb|ps|pt|d)_\\d+\"");
        Matcher matcher = pattern.matcher(xmlResponse);
        StringBuffer modifiedString = new StringBuffer();
        int lineNumber = 1;
        int replacementIndex = 0;
        while (matcher.find()) {
            String replacement = '"' + replacements[replacementIndex] + '"';
            matcher.appendReplacement(modifiedString, Matcher.quoteReplacement(replacement));
            replacementIndex = (replacementIndex + 1) % replacements.length;

        }
        matcher.appendTail(modifiedString);

        return modifiedString.toString();
    }

}
