package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.model.Staff.DeleteStaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.service.StaffAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
public class StaffAccountController {

    @Autowired
    StaffAccountService staffAccountService;

    // Create a new staff account
    @PostMapping("staff")
    public ResponseEntity<StaffAccountResponse> createStaffAccount(@RequestBody CreateStaffAccountRequest createStaffAccountRequest) {
        StaffAccountResponse newStaffAccount = staffAccountService.createStaffAccount(createStaffAccountRequest);
        return ResponseEntity.ok(newStaffAccount);
    }

    // Read all staff accounts
    @GetMapping("staff")
    public ResponseEntity<List<StaffAccountResponse>> readAllStaffAccounts() {
        List<StaffAccountResponse> staffAccounts = staffAccountService.readAllStaffAccounts();
        return ResponseEntity.ok(staffAccounts);
    }

    // Read a staff account by ID
    @GetMapping("staff/{id}")
    public ResponseEntity<StaffAccountResponse> getStaffAccountById(@PathVariable long id) {
        StaffAccountResponse staffAccount = staffAccountService.getStaffAccountById(id);
        if (staffAccount != null) {
            return ResponseEntity.ok(staffAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing staff account
    @PutMapping("staff/{id}")
    public ResponseEntity<StaffAccount> updateStaffAccount(@PathVariable long id, @RequestBody StaffAccountRequest staffAccountRequest) {
        staffAccountRequest.setStaffID(id); // Ensure the ID from the path is set in the request
        StaffAccount updatedStaffAccount = staffAccountService.updateStaffAccount(staffAccountRequest);
        return ResponseEntity.ok(updatedStaffAccount);
    }

    // Deactivate a staff account
    @DeleteMapping("staff/{id}")
    public ResponseEntity<String> deactivateStaffAccount(@PathVariable long id) {// Ensure the ID from the path is set in the request
        staffAccountService.deactivateStaffAccount(id);
        return ResponseEntity.noContent().build();
    }

}
