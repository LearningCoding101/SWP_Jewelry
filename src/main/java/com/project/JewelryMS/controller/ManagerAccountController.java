package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import com.project.JewelryMS.service.ManagerAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("manager")
public class ManagerAccountController {

    @Autowired
    ManagerAccountService managerAccountService;

    @GetMapping("/readAll")
    public ResponseEntity<List<ManagerAccountResponse>> readAllManagerAccounts() {
        return ResponseEntity.ok(managerAccountService.getAllManagerAccounts());
    }

    @GetMapping("/readbyid")
    public ResponseEntity<ManagerAccountResponse> readManagerAccountById(@RequestParam("id") int id) {
        ManagerAccountResponse managerAccountResponse = managerAccountService.getManagerAccountById(id);
        if (managerAccountResponse != null) {
            return ResponseEntity.ok(managerAccountResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
