package com.project.JewelryMS.controller;

import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.DeleteShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.service.ShiftService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("shift")
@SecurityRequirement(name = "api")
public class ShiftController {
    @Autowired
    private ShiftService shiftService;

    // Create Shift
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ShiftRequest> createShift(@RequestBody CreateShiftRequest createShiftRequest) {
        return ResponseEntity.ok(shiftService.createShift(createShiftRequest));
    }

    // Read All Shifts
    @GetMapping("/read")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getAllShifts() {
        return ResponseEntity.ok(shiftService.readAllShifts());
    }

    // Read Shift by ID
    @GetMapping("readById")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<ShiftRequest> getShiftById(@RequestParam("id") int id) {
        return ResponseEntity.ok(shiftService.getShiftById(id));
    }

    // Read Shifts by start time
    @GetMapping("/read/startTime")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByStartTime(@RequestParam("startTime") String startTime) {
        return ResponseEntity.ok(shiftService.getShiftsByStartTime(startTime));
    }

    // Read Shifts by end time
    @GetMapping("/read/endTime")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByEndTime(@RequestParam("endTime") String endTime) {
        return ResponseEntity.ok(shiftService.getShiftsByEndTime(endTime));
    }

    // Read Shifts by shift type
    @GetMapping("/read/shiftType")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByShiftType(@RequestParam("shiftType") String shiftType) {
        return ResponseEntity.ok(shiftService.getShiftsByShiftType(shiftType));
    }

    // Read Shifts by status
    @GetMapping("/read/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByStatus(@RequestParam("status") String status) {
        return ResponseEntity.ok(shiftService.getShiftsByStatus(status));
    }

    // Read Shifts by register
    @GetMapping("/read/register")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByRegister(@RequestParam("register") int register) {
        return ResponseEntity.ok(shiftService.getShiftsByRegister(register));
    }

    // Read Shifts by work area
    @GetMapping("/read/workArea")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_STAFF')")
    public ResponseEntity<List<ShiftRequest>> getShiftsByWorkArea(@RequestParam("workArea") String workArea) {
        return ResponseEntity.ok(shiftService.getShiftsByWorkArea(workArea));
    }

    //Delete section
    @PatchMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> deletePerformanceById(@RequestBody DeleteShiftRequest deleteShiftRequest){
        shiftService.deleteShiftById(deleteShiftRequest);
        return ResponseEntity.ok("Shift details marked as invalid successfully");
    }


    //Update section
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_MANAGER')")
    public ResponseEntity<String> updatePerformance(@PathVariable int id, @RequestBody ShiftRequest shiftRequest) {
        shiftRequest.setShiftID(id); // Ensure the ID from the path is set in the request
        shiftService.updateShiftDetails(shiftRequest);
        return ResponseEntity.ok("Shift Details updated successfully");
    }
}
