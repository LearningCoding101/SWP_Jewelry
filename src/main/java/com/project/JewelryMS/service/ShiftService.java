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
import java.util.stream.Collectors;

@Service
public class ShiftService {
    @Autowired
    ShiftRepository shiftRepository;

    // Helper method to convert a Shift entity to a ShiftRequest DTO
    private ShiftRequest toShiftRequest(Shift shift) {
        return new ShiftRequest(
                shift.getShiftID(),
                shift.getEndTime(),
                shift.getRegister(),
                shift.getShiftType(),
                shift.getStartTime(),
                shift.getStatus(),
                shift.getWorkArea()
        );
    }

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
        return toShiftRequest(newShiftAddition);
    }

    // Read all Shifts
    public List<ShiftRequest> readAllShifts() {
        List<Shift> shifts = shiftRepository.listAll();

        return shifts.stream()
                .map(this::toShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shift by ID
    public ShiftRequest getShiftById(Integer id) {
        Shift shift = shiftRepository.findByShiftId(id);
        return shift != null ? toShiftRequest(shift) : null;
    }

    // Read Shifts by start time
    public List<ShiftRequest> getShiftsByStartTime(Timestamp startTime) {
        List<Shift> shifts = shiftRepository.findByStartTime(startTime);

        return shifts.stream()
                .map(this::toShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shifts by end time
    public List<ShiftRequest> getShiftsByEndTime(Timestamp endTime) {
        List<Shift> shifts = shiftRepository.findByEndTime(endTime);

        return shifts.stream()
                .map(this::toShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shifts by shift type
    public List<ShiftRequest> getShiftsByShiftType(String shiftType) {
        List<Shift> shifts = shiftRepository.findByShiftType(shiftType);

        return shifts.stream()
                .map(this::toShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shifts by status
    public List<ShiftRequest> getShiftsByStatus(String status) {
        List<Shift> shifts = shiftRepository.findByStatus(status);

        return shifts.stream()
                .map(this::toShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shifts by register
    public List<ShiftRequest> getShiftsByRegister(int register) {
        List<Shift> shifts = shiftRepository.findByRegister(register);

        return shifts.stream()
                .map(this::toShiftRequest)
                .collect(Collectors.toList());
    }

    // Read Shifts by work area
    public List<ShiftRequest> getShiftsByWorkArea(String workArea) {
        List<Shift> shifts = shiftRepository.findByWorkArea(workArea);

        return shifts.stream()
                .map(this::toShiftRequest)
                .collect(Collectors.toList());
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

