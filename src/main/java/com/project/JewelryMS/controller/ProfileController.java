package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Profile.*;
import com.project.JewelryMS.service.Profile.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
public class ProfileController {

    @Autowired
    ProfileService profileService;


    @PutMapping("/managerprofile/{managerId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<UpdateManagerResponse> updateManagerProfile(@PathVariable Long managerId,
                                                                      @RequestBody UpdateManagerResponse updateManagerRequest) {
        UpdateManagerResponse updatedProfile = profileService.updateManagerProfile(managerId, updateManagerRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/adminprofile/{adminId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AdminProfileResponse> viewAdminProfile(@PathVariable Long adminId) {
        AdminProfileResponse profile = profileService.viewAdminProfile(adminId);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/adminprofile/{adminId}")
    public ResponseEntity<UpdateAdminResponse> updateAdminProfile(@PathVariable Long adminId,
                                                                  @RequestBody UpdateAdminResponse updateAdminRequest) {
        UpdateAdminResponse updatedProfile = profileService.updateAdminProfile(adminId, updateAdminRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/staffprofile/{staffId}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<UpdateStaffResponse> updateStaffProfile(@PathVariable Integer staffId,
                                                                  @RequestBody UpdateStaffResponse updateStaffRequest) {
        UpdateStaffResponse updatedProfile = profileService.updateStaffProfile(staffId, updateStaffRequest);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/staffshift/{staffId}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<ViewShiftResponse> viewStaffShiftProfile(@PathVariable Integer staffId) {
        ViewShiftResponse response = profileService.viewShiftProfile(staffId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build(); // Staff not found or doesn't have a shift
        }
    }

}
