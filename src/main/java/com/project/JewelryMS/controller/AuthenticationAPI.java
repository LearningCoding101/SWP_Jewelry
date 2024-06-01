package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.model.*;
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
        Account account = null;
        if(authenticationService.handleRegisterCheckEmail(registerRequest)){
            account = authenticationService.register(registerRequest);
        }
        if(account == null){
            return ResponseEntity.badRequest().body("Invalid Email or Information");
        }
        return ResponseEntity.ok("Account created");
    }

    //login
    @PostMapping("login")
    public ResponseEntity<AccountResponse> login(@RequestBody LoginRequest loginRequest){
        AccountResponse accountResponse = authenticationService.login(loginRequest);

        return ResponseEntity.ok(accountResponse);
    }
    //Change password
    /*@PostMapping("change")
    public ResponseEntity changePassword(@RequestBody ChangePassRequest changePassRequest){

    }*/

    //ResetPassword
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassRequest email){
        return ResponseEntity.ok(authenticationService.sendForgotPasswordEmail(email.getEmail()));
    }

    //Read All Manager Account
    @GetMapping("/managers")
    public ResponseEntity<List<Account>> getAllManagerAccounts() {
        List<Account> managerAccounts = authenticationService.getAllManagerAccount();
        return ResponseEntity.ok(managerAccounts);
    }
}
