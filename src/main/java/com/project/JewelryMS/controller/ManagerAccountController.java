package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.service.ManagerAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("manager")
@SecurityRequirement(name = "api")
public class ManagerAccountController {

    @Autowired
    ManagerAccountService managerAccountService;
    //Read All Manager Account
    @GetMapping("/read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Account>> getAllManagerAccounts() {
        List<Account> managerAccounts = managerAccountService.getAllManagerAccount();
        return ResponseEntity.ok(managerAccounts);
    }


}
