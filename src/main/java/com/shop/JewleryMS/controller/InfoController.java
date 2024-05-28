package com.shop.JewleryMS.controller;

import com.shop.JewleryMS.service.ApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Info")
@SecurityRequirement(name = "api")

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
