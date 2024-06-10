package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Shift;

import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.DeleteShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
//import java.security.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ShiftService {
    @Autowired
    ShiftRepository shiftRepository;

    // Create Shift
    public ShiftRequest createShift(CreateShiftRequest createShiftRequest) {
        Shift newShift = new Shift();

        newShift.setEndTime(createShiftRequest.getEndTime());
        newShift.setRegister(createShiftRequest.getRegister());
        newShift.setShiftType(createShiftRequest.getShiftType());
        newShift.setStartTime(createShiftRequest.getStartTime());
        newShift.setStatus(createShiftRequest.getStatus());
        newShift.setWorkArea(createShiftRequest.getWorkArea());

        Shift newShiftAddition= shiftRepository.save(newShift);
        return getShiftById(newShiftAddition.getShiftID());
    }

    // Read all Shifts
    public List<ShiftRequest> readAllShifts() {
        return shiftRepository.listAll();
    }

    // Read Shift by ID
    public ShiftRequest getShiftById(Integer id) {
        Optional<ShiftRequest> shiftOptional = shiftRepository.findByShiftId(id);
        return shiftOptional.orElse(null);
    }

    // Read Shifts by start time
    public List<ShiftRequest> getShiftsByStartTime(Timestamp startTime) {
        return shiftRepository.findByStartTime(startTime);
    }

    // Read Shifts by end time
    public List<ShiftRequest> getShiftsByEndTime(Timestamp endTime) {
        return shiftRepository.findByEndTime(endTime);
    }

    // Read Shifts by shift type
    public List<ShiftRequest> getShiftsByShiftType(String shiftType) {
        return shiftRepository.findByShiftType(shiftType);
    }

    // Read Shifts by status
    public List<ShiftRequest> getShiftsByStatus(String status) {
        return shiftRepository.findByStatus(status);
    }

    // Read Shifts by register
    public List<ShiftRequest> getShiftsByRegister(int register) {
        return shiftRepository.findByRegister(register);
    }

    // Read Shifts by work area
    public List<ShiftRequest> getShiftsByWorkArea(String workArea) {
        return shiftRepository.findByWorkArea(workArea);
    }

    public Shift updateShiftDetails(ShiftRequest shiftRequest) {
        Optional<Shift> shiftUpdate = shiftRepository.findById(shiftRequest.getShiftID());

        if(shiftUpdate.isPresent()){
            Shift shift = shiftUpdate.get();

            shift.setEndTime(shiftRequest.getEndTime());
            shift.setRegister(shiftRequest.getRegister());
            shift.setShiftType(shiftRequest.getShiftType());
            shift.setStartTime(shiftRequest.getStartTime());
            shift.setStatus(shiftRequest.getStatus());
            shift.setWorkArea(shiftRequest.getWorkArea());

            return shiftRepository.save(shift);
        }else {
            throw new RuntimeException("Staff shift with ID " + shiftRequest.getShiftID() + " not found");
        }
    }

    public void deleteShiftById(DeleteShiftRequest deleteShiftRequest) {
        Optional<Shift> shiftOptional = shiftRepository.findById(deleteShiftRequest.getShiftID());
        if (shiftOptional.isPresent()) {
            Shift shift = shiftOptional.get();
            if (shift != null) {
                String status="Unapplicable";
                shift.setStatus(status);
                shiftRepository.save(shift);
            } else {
                throw new RuntimeException("Shift ID:  " + deleteShiftRequest.getShiftID() + " not found");
            }
        } else {
            throw new RuntimeException("Shift ID" + deleteShiftRequest.getShiftID()+ " not found");
        }
    }




}
