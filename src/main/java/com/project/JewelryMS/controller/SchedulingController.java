package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Staff_Shift;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.JewelryMS.service.SchedulingService;

@RestController
@RequestMapping("scheduling")
@SecurityRequirement(name = "api")
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("/assignStaffToShift")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<Staff_Shift> assignStaffToShift(@RequestParam int staffId, @RequestParam int shiftId) {
        Staff_Shift staffShift = schedulingService.assignStaffToShift(staffId, shiftId);
        return ResponseEntity.ok(staffShift);
    }

    @PostMapping("/assignShiftToStaff")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<Staff_Shift> assignShiftToStaff(@RequestParam int shiftId, @RequestParam int staffId) {
        Staff_Shift staffShift = schedulingService.assignShiftToStaff(shiftId, staffId);
        return ResponseEntity.ok(staffShift);
    }
}
