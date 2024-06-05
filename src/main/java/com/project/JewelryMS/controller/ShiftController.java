package com.project.JewelryMS.controller;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.service.ShiftService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("shift")
@SecurityRequirement(name = "api")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    // Create Shift
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Shift> createShift(@RequestBody CreateShiftRequest createShiftRequest) {
        return ResponseEntity.ok(shiftService.createShift(createShiftRequest));
    }

    // Read All Shifts
    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<List<Shift>> getAllShifts() {
        return ResponseEntity.ok(shiftService.readAllShifts());
    }

    // Read Shift by ID
    @GetMapping("readbyid")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Shift> getShiftById(@RequestParam("id") int id) {
        return ResponseEntity.ok(shiftService.getShiftById(id));
    }
}
