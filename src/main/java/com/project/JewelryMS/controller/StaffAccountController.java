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


    // Read all staff accounts
    @GetMapping("staff/readall")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<StaffAccountResponse>> readAllStaffAccounts() {
        List<StaffAccountResponse> staffAccounts = staffAccountService.readAllStaffAccounts();
        return ResponseEntity.ok(staffAccounts);
    }

    // Read a staff account by ID
    @GetMapping("staff/read/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<StaffAccountResponse> getStaffAccountById(@PathVariable Integer id) {
        StaffAccountResponse staffAccount = staffAccountService.getStaffAccountById(id);
        if (staffAccount != null) {
            return ResponseEntity.ok(staffAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing staff account
    @PutMapping("staff/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<StaffAccountResponse> updateStaffAccount(@PathVariable Integer id, @RequestBody StaffAccountRequest staffAccountRequest) {
        StaffAccountResponse updatedStaffAccount = staffAccountService.updateStaffAccount(id,staffAccountRequest);
        return ResponseEntity.ok(updatedStaffAccount);
    }

    // Deactivate a staff account
    @DeleteMapping("staff/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<String> deactivateStaffAccount(@PathVariable Integer id) {// Ensure the ID from the path is set in the request
        staffAccountService.deactivateStaffAccount(id);
        return ResponseEntity.ok("Delete Successfully");
    }

}
