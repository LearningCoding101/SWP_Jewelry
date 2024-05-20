package com.shop.JewleryMS;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class testAgain {
    @GetMapping("/Hoang")
    public String test(){
        return "Testt AGian";
    }
}
