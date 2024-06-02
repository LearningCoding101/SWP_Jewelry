package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.model.Staff.CreateStaffAccountRequest;
import com.project.JewelryMS.model.Staff.DeleteStaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.service.StaffAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("staff")
//@SecurityRequirement(name = "api")
public class StaffAccountController {

    @Autowired
    StaffAccountService staffAccountService;

    // Create a new StaffAccount
    @PostMapping("create")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffAccount> createStaffAccount(@RequestBody CreateStaffAccountRequest createStaffAccountRequest) {
        StaffAccount createdStaff = staffAccountService.createStaffAccount(createStaffAccountRequest);
        return ResponseEntity.ok(createdStaff);
    }

    // Read all StaffAccounts
    @GetMapping("read")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StaffAccountResponse>> readAllStaffAccounts() {
        System.out.println("Read Staff");
        return ResponseEntity.ok(staffAccountService.readAllStaffAccounts());
    }

    // Update a StaffAccount
    @PostMapping("update")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffAccount> updateStaffAccount(@RequestBody StaffAccountRequest staffAccountRequest) {
        try {
            StaffAccount updatedStaff = staffAccountService.updateStaffAccount(staffAccountRequest);
            return ResponseEntity.ok(updatedStaff);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get a specific StaffAccount by ID
    @GetMapping("readbyid")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffAccountResponse> getStaffAccountById(@RequestParam("id") int id) {
        StaffAccountResponse staffAccount = staffAccountService.getStaffAccountById(id);
        if (staffAccount != null) {
            return ResponseEntity.ok(staffAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateStaffAccount(@RequestBody DeleteStaffAccountRequest deleteStaffAccountRequest) {
        try {
            staffAccountService.deactivateStaffAccount(deleteStaffAccountRequest);
            return ResponseEntity.ok("StaffAccount deactivated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
