package com.shop.JewleryMS.controller;

import com.shop.JewleryMS.entity.Account;
import com.shop.JewleryMS.model.RegisterRequest;
import com.shop.JewleryMS.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Account")
public class AuthenticationAPI {
    @Autowired
    AuthenticationService authenticationService;
    @GetMapping("Authentication")
    public ResponseEntity<String> test(){
        System.out.println("Reached!");
        return ResponseEntity.ok("test");
    }
    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest){
        System.out.println("Reached");
        Account account = authenticationService.register(registerRequest);

        return ResponseEntity.ok(account);
    }

    //login

    //Change password


    //ResetPassword

 }
