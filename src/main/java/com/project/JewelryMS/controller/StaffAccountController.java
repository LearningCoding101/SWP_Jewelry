package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.CreateStaffAccountRequest;
import com.project.JewelryMS.model.StaffAccountRequest;
import com.project.JewelryMS.service.StaffAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("staff")
@SecurityRequirement(name = "api")
public class StaffAccountController {

    @Autowired
    StaffAccountService staffAccountService;

    // Create a new StaffAccount
    @PostMapping("create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffAccount> createStaffAccount(@RequestBody CreateStaffAccountRequest createStaffAccountRequest) {
        StaffAccount createdStaff = staffAccountService.createStaffAccount(createStaffAccountRequest);
        return ResponseEntity.ok(createdStaff);
    }

    // Read all StaffAccounts
    @GetMapping("read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffAccount>> readAllStaffAccounts() {
        return ResponseEntity.ok(staffAccountService.readAllStaffAccounts());
    }

    // Update a StaffAccount
    @PostMapping("update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffAccount> updateStaffAccount(@RequestBody StaffAccountRequest staffAccountRequest) {
        try {
            StaffAccount updatedStaff = staffAccountService.updateStaffAccount(staffAccountRequest);
            return ResponseEntity.ok(updatedStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get a specific StaffAccount by ID
    @GetMapping("/read/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffAccount> getStaffAccountById(@RequestParam("id") long id) {
        StaffAccount staffAccount = staffAccountService.getStaffAccountById(id);
        if (staffAccount != null) {
            return ResponseEntity.ok(staffAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateStaffAccount(@RequestParam("id") long id) {
        try {
            staffAccountService.deactivateStaffAccount(id);
            return ResponseEntity.ok("StaffAccount deactivated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
