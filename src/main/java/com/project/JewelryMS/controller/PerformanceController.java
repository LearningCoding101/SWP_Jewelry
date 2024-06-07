package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Performance;
import com.project.JewelryMS.model.Performance.CreatePerformanceRequest;
import com.project.JewelryMS.model.Performance.DeletePerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceRequest;
import com.project.JewelryMS.model.Performance.PerformanceResponse;
import com.project.JewelryMS.service.PerformanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@RestController
@RequestMapping("/staff-performance")
@SecurityRequirement(name = "api")

public class PerformanceController {
    @Autowired
    PerformanceService performanceService;

    //Create section
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<PerformanceResponse> createPerformanceReport(@RequestBody CreatePerformanceRequest createPerformanceReport) {
        PerformanceResponse performance = performanceService.createPerformanceReport(createPerformanceReport);
        return ResponseEntity.ok(performance);
    }

    //Read section
    @GetMapping("/list-all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PerformanceResponse>>readAllCustomer(){
        List<PerformanceResponse> performanceList = performanceService.readAllPerformanceReport();
        return ResponseEntity.ok(performanceList);
    }

    @GetMapping("/list-by-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<PerformanceResponse> readPerformanceFromId(@RequestParam Long id){
        PerformanceResponse performance = performanceService.getPerformanceById(id);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/list-by-staff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<PerformanceResponse> readPerformanceByStaff(@RequestParam Long id){
        PerformanceResponse performance = performanceService.getPerformanceByStaffID(id);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/list-by-date")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PerformanceResponse>> readPerformanceByDate( @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date targetDate){
        List<PerformanceResponse> performance = performanceService.getPerformanceByDate(targetDate);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/list-report-valid")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<List<PerformanceResponse>> readAllActiveReports() {
        List<PerformanceResponse> customerList = performanceService.readAllConfirmedPerformanceReport(); // Filter active customers
        return ResponseEntity.ok(customerList);
    }


    //Delete section
    @PatchMapping("/delete-status-by-id")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deletePerformanceById(@RequestBody DeletePerformanceRequest deletePerformanceRequest){
        performanceService.deletePerformanceById(deletePerformanceRequest);
        return ResponseEntity.ok("Performance details marked as invalid successfully");
    }


    //Update section
    @PutMapping("/update-performance-details/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updatePerformance(@PathVariable long id, @RequestBody PerformanceRequest performanceRequest) {
        performanceRequest.setPK_performanceID(id); // Ensure the ID from the path is set in the request
        performanceService.updatePerformanceReportDetails(performanceRequest);
        return ResponseEntity.ok("Performance Details updated successfully");
    }


}
