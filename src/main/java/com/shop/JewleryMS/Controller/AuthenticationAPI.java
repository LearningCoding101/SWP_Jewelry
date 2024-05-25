package com.shop.JewleryMS.Controller;

import com.shop.JewleryMS.Entity.Account;
import com.shop.JewleryMS.Model.RegisterRequest;
import com.shop.JewleryMS.Service.AuthenticationService;
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
