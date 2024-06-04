package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Guarantee;
import com.project.JewelryMS.model.CreateGuaranteeRequest;
import com.project.JewelryMS.model.GuaranteeRequest;
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

    //Create section
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Guarantee>createGuarantee(@RequestBody CreateGuaranteeRequest createGuaranteeRequest) {
        Guarantee promotion = guaranteeService.createGuarantee(createGuaranteeRequest);
        return ResponseEntity.ok(promotion);
    }

    //Read section
    @GetMapping("/list-all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<Guarantee>>readAllGuarantee(){
        List<Guarantee> promotionList = guaranteeService.readAllGuarantee();
        return ResponseEntity.ok(promotionList);
    }

    @GetMapping("/list-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<Guarantee> readGuaranteeById(@RequestParam Long id){
        Guarantee promotion = guaranteeService.getGuaranteeById(id);//fix
        return ResponseEntity.ok(promotion);
    }


    //Delete section
    @PatchMapping("/delete-status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deleteGuarantee(@RequestBody long id){
        guaranteeService.deleteGuaranteePolicyById(id);
        return ResponseEntity.ok("Guarantee policy details marked as inactive successfully");
    }

    //Update section
    @PutMapping("/update-details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updateGuaranteeDetails(@RequestBody GuaranteeRequest guaranteeRequest){
        guaranteeService.updateGuaranteeDetails(guaranteeRequest);
        return ResponseEntity.ok("Guarantee Policy Details updated successfully");
    }

    //1 more api to apply guarantee to product can be created/used here
}
