package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Guarantee.CreateGuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeRequest;
import com.project.JewelryMS.model.Guarantee.GuaranteeResponse;
import com.project.JewelryMS.service.GuaranteeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guarantee")
@SecurityRequirement(name = "api")
public class GuaranteeController {

    @Autowired
    GuaranteeService guaranteeService;

    // Create section
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<GuaranteeResponse> createGuarantee(@RequestBody CreateGuaranteeRequest createGuaranteeRequest) {
        GuaranteeResponse guarantee = guaranteeService.createGuarantee(createGuaranteeRequest);
        return ResponseEntity.ok(guarantee);
    }

    // Read section
    @GetMapping("/list-all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readAllGuarantees() {
        List<GuaranteeResponse> guaranteeList = guaranteeService.readAllGuaranteeResponses();
        return ResponseEntity.ok(guaranteeList);
    }

    @GetMapping("/list-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<GuaranteeResponse> readGuaranteeById(@RequestParam Long id) {
        GuaranteeResponse guarantee = guaranteeService.getGuaranteeResponseById(id);
        return ResponseEntity.ok(guarantee);
    }

    @GetMapping("/list-active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readAllActiveGuarantees() {
        List<GuaranteeResponse> activeGuarantees = guaranteeService.readAllActiveGuaranteePolicies();
        return ResponseEntity.ok(activeGuarantees);
    }

    @GetMapping("/list-inactive")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readAllInactiveGuarantees() {
        List<GuaranteeResponse> inactiveGuarantees = guaranteeService.readAllInactiveGuaranteePolicies();
        return ResponseEntity.ok(inactiveGuarantees);
    }

    @GetMapping("/list-policy-type")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readGuaranteesByPolicyType(@RequestParam String policyType) {
        List<GuaranteeResponse> guaranteesByPolicyType = guaranteeService.readAllGuaranteesByPolicyType(policyType);
        return ResponseEntity.ok(guaranteesByPolicyType);
    }

    @GetMapping("/list-coverage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<GuaranteeResponse>> readGuaranteesByCoverage(@RequestParam String coverage) {
        List<GuaranteeResponse> guaranteesByCoverage = guaranteeService.readAllGuaranteesByCoverage(coverage);
        return ResponseEntity.ok(guaranteesByCoverage);
    }

    @GetMapping("/list-product-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<GuaranteeResponse> readGuaranteeByProductId(@RequestParam Long productId) {
        GuaranteeResponse guarantee = guaranteeService.readGuaranteeByProductId(productId);
        return ResponseEntity.ok(guarantee);
    }

    // Delete section
    @PatchMapping("/delete-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteGuarantee(@RequestParam long id) {
        guaranteeService.deleteGuaranteePolicyById(id);
        return ResponseEntity.ok("Guarantee policy details marked as inactive successfully");
    }

    // Update section
    @PutMapping("/update-details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateGuaranteeDetails(@RequestBody GuaranteeRequest guaranteeRequest) {
        guaranteeService.updateGuaranteeDetails(guaranteeRequest);
        return ResponseEntity.ok("Guarantee policy details updated successfully");
    }
//1 more api to apply guarantee to product can be created/used here

}