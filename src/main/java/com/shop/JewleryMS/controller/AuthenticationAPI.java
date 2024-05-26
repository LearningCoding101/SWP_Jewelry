package com.shop.JewleryMS.controller;

import com.shop.JewleryMS.entity.Account;
import com.shop.JewleryMS.model.AccountResponse;
import com.shop.JewleryMS.model.EmailDetail;
import com.shop.JewleryMS.model.LoginRequest;
import com.shop.JewleryMS.model.RegisterRequest;
import com.shop.JewleryMS.service.AuthenticationService;
import com.shop.JewleryMS.service.EmailService;
import com.shop.JewleryMS.service.JWTservice;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("account")
@SecurityRequirement(name = "api")
public class AuthenticationAPI {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    EmailService emailService;
    @GetMapping("testGetUserByEmail")
    public ResponseEntity getByEmail(@RequestParam String email){
        System.out.println("Reached");
        Account account = authenticationService.getAccount(email);

        return ResponseEntity.ok(account);
    }
    @GetMapping("testGetAll")
    public ResponseEntity getAll(){
        System.out.println("Reached");
        List<Account> account = authenticationService.getAllAccount();

        return ResponseEntity.ok(account);
    }


    @GetMapping("authentication")
    public ResponseEntity<String> test2(){
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
    @PostMapping("login")
    public ResponseEntity<AccountResponse> login(@RequestBody LoginRequest loginRequest){
        AccountResponse accountResponse = authenticationService.login(loginRequest);

        return ResponseEntity.ok(accountResponse);
    }
    //Change password


    //ResetPassword
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody String email){
        authenticationService.sendForgotPasswordEmail(email);
        return ResponseEntity.ok("New password sent");
    }

    @GetMapping("test")
    public void sendMail(){
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("hahoang433322@gmail.com");
        emailDetail.setSubject("test123");
        emailDetail.setMsgBody("aaa");
        emailService.sendMailTemplate(emailDetail);
    }
 }
