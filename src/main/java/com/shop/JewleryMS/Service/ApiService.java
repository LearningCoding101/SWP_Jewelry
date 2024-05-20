package com.shop.JewleryMS.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.hibernate.type.format.jackson.JacksonXmlFormatMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper jsonMapper;
    private final XmlMapper xmlMapper;

    public ApiService(RestTemplate restTemplate,
                      ObjectMapper jsonMapper,
                      XmlMapper xmlMapper) {
        this.restTemplate = restTemplate;
        this.jsonMapper = jsonMapper;
        this.xmlMapper = xmlMapper;
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
