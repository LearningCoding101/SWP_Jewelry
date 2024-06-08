package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.model.Manager.CreateManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountRequest;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import com.project.JewelryMS.service.ManagerAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
public class ManagerAccountController {

    @Autowired
    ManagerAccountService managerAccountService;

    @GetMapping("manager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ManagerAccountResponse>> readAllManagerAccounts() {
        return ResponseEntity.ok(managerAccountService.getAllManagerAccounts());
    }
    // Get a manager account by ID
    @GetMapping("manager/read/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ManagerAccountResponse> getManagerAccountById(@PathVariable Integer id) {
        ManagerAccountResponse managerAccount = managerAccountService.getManagerAccountById(id);
        return ResponseEntity.ok(managerAccount);
    }

    // Create a new manager account
    @PostMapping("manager/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerAccountResponse> createManagerAccount(@RequestBody CreateManagerAccountRequest createManagerAccountRequest) {
        ManagerAccountResponse newManagerAccount = managerAccountService.createManagerAccount(createManagerAccountRequest);
        return ResponseEntity.ok(newManagerAccount);
    }

    // Update an existing manager account
    @PutMapping("manager/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ManagerAccountResponse> updateManagerAccount(@PathVariable Integer id, @RequestBody ManagerAccountRequest managerAccountRequest) {
        ManagerAccountResponse updatedManagerAccount = managerAccountService.updateManagerAccount(id,managerAccountRequest);
        return ResponseEntity.ok(updatedManagerAccount);
    }

    // Deactivate a manager account
    @DeleteMapping("manager/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateManagerAccount(@PathVariable Integer id) {
        return ResponseEntity.ok(managerAccountService.deactivateManagerAccount(id));
    }

}
