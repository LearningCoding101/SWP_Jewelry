package com.shop.JewleryMS.controller;

import com.shop.JewleryMS.entity.StaffAccount;
import com.shop.JewleryMS.model.CreateStaffAccountRequest;
import com.shop.JewleryMS.model.StaffAccountRequest;
import com.shop.JewleryMS.service.StaffAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Staffaccount")
@SecurityRequirement(name = "api")
public class StaffAccountController {

    @Autowired
    StaffAccountService staffAccountService;

    @PostMapping("Create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StaffAccount> CreateStaffAccount(@RequestBody CreateStaffAccountRequest createStaffAccountRequest){
        return ResponseEntity.ok(staffAccountService.CreateStaffAccount(createStaffAccountRequest));
    }

    @GetMapping("Read")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<StaffAccount>> ReadStaffAccount(){
        return ResponseEntity.ok(staffAccountService.ReadStaffAccounts());
    }

    @PostMapping("Update")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<String> UpdateStaffAccount(@RequestBody StaffAccountRequest staffAccountRequest){
        staffAccountService.UpdateStaffAccount(staffAccountRequest);
        return ResponseEntity.ok("Update Successfully !!!");
    }

}
