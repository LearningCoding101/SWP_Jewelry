package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {
    @Autowired
    ShiftRepository shiftRepository;


    // Create Shift
    public Shift createShift(CreateShiftRequest createShiftRequest) {
        Shift newShift = new Shift();
        newShift.setEndTime(createShiftRequest.getEndTime());
        newShift.setRegister(createShiftRequest.getRegister());
        newShift.setShiftType(createShiftRequest.getShiftType());
        newShift.setStartTime(createShiftRequest.getStartTime());
        newShift.setStatus(createShiftRequest.getStatus());
        newShift.setWorkArea(createShiftRequest.getWorkArea());
        return shiftRepository.save(newShift);
    }

    // Read all Shifts
    public List<Shift> readAllShifts() {
        return shiftRepository.findAll();
    }

    // Read Shift by ID
    public Shift getShiftById(long id) {
        Optional<Shift> shiftOptional = shiftRepository.findById(id);
        return shiftOptional.orElse(null);
    }


}
