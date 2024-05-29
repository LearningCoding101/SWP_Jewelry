
package com.shop.JewleryMS.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("test")
    public ResponseEntity<String> Test(){
        return ResponseEntity.ok("test");
    }
}
