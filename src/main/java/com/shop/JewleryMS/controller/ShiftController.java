package com.shop.JewleryMS.controller;

import com.shop.JewleryMS.entity.Shift;
import com.shop.JewleryMS.model.CreateShiftRequest;
import com.shop.JewleryMS.repository.ShiftRepository;
import com.shop.JewleryMS.service.ShiftService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Shift")
@SecurityRequirement(name = "api")
public class ShiftController {

    @Autowired
    ShiftService shiftService;

    @PostMapping("Create")
    public ResponseEntity<Shift> CreateShift(@RequestBody CreateShiftRequest createShiftRequest){
        return ResponseEntity.ok(shiftService.CreateShift(createShiftRequest));
    }
}
