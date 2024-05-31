package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.model.AccountResponse;
import com.project.JewelryMS.model.LoginRequest;
import com.project.JewelryMS.model.RegisterRequest;
import com.project.JewelryMS.service.AuthenticationService;
import com.project.JewelryMS.service.EmailService;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity getByEmail(@RequestParam String email){
        System.out.println("Reached");
        Account account = authenticationService.getAccount(email);

        return ResponseEntity.ok(account);
    }
    @GetMapping("testGetAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Account>> getAll(){
        System.out.println("Reached");
        List<Account> account = authenticationService.getAllAccount();

        return ResponseEntity.ok(account);
    }


    @GetMapping("authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @GetMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email){

        return ResponseEntity.ok(authenticationService.sendForgotPasswordEmail(email));
    }


}
