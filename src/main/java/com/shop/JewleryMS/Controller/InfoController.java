package com.shop.JewleryMS.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.shop.JewleryMS.Service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Info")
public class InfoController {
    @Autowired
    private ApiService apiService;

    private final String goldUrl = "http://api.btmc.vn/api/BTMCAPI/getpricebtmc?key=3kd8ub1llcg9t45hnoh8hmn7t5kc2v";
    @GetMapping("/GoldPrice")
    @CrossOrigin(origins = "http://localhost:5173")
    public String getGoldPrice(){
        System.out.println("Pinged /GoldPrice");
        return apiService.getGoldPrice(goldUrl);
    }
}
