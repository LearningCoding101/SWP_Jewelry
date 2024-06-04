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
    @GetMapping("manager/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ManagerAccountResponse> getManagerAccountById(@PathVariable int id) {
        ManagerAccountResponse managerAccount = managerAccountService.getManagerAccountById(id);
        return ResponseEntity.ok(managerAccount);
    }

    // Create a new manager account
    @PostMapping("manager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ManagerAccountResponse> createManagerAccount(@RequestBody CreateManagerAccountRequest createManagerAccountRequest) {
        ManagerAccountResponse newManagerAccount = managerAccountService.createManagerAccount(createManagerAccountRequest);
        return ResponseEntity.ok(newManagerAccount);
    }

    // Update an existing manager account
    @PutMapping("manager/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Account> updateManagerAccount(@PathVariable int id, @RequestBody ManagerAccountRequest managerAccountRequest) {
        managerAccountRequest.setUser_ID(id); // Ensure the ID from path is set in the request
        Account updatedManagerAccount = managerAccountService.updateManagerAccount(managerAccountRequest);
        return ResponseEntity.ok(updatedManagerAccount);
    }

    // Deactivate a manager account
    @DeleteMapping("manager/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateManagerAccount(@PathVariable long id) {
        managerAccountService.deactivateManagerAccount(id);
        return ResponseEntity.ok("Delete Successfully");
    }

}
