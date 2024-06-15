package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.repository.StaffShiftRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@Service
public class SchedulingService {

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private StaffShiftRepository staffShiftRepository;

    // Method to assign a staff member to a shift
    @Transactional
    public Staff_Shift assignStaffToShift(int staffId, long shiftId) {
        // Fetch the staff and shift entities from the database
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        // Check if the staff member is already assigned to the shift
        if (staff.getStaffShifts().stream().anyMatch(ss -> ss.getShift().equals(shift))) {
            throw new RuntimeException("Staff is already assigned to this shift");
        }

        // Create a new Staff_Shift entity
        Staff_Shift staffShift = new Staff_Shift();
        staffShift.setStaffAccount(staff);
        staffShift.setShift(shift);

        // Save the new Staff_Shift entity to the database
        return staffShiftRepository.save(staffShift);
    }

    // Method to assign a shift to a staff member
    @Transactional
    public Staff_Shift assignShiftToStaff(int shiftId, int staffId) {
        return assignStaffToShift(staffId, shiftId);
    }

    public Map<LocalDate, List<StaffAccount>> getScheduleMatrix(LocalDate startDate, LocalDate endDate) {
        // Initialize the matrix as a LinkedHashMap to maintain the insertion order
        Map<LocalDate, List<StaffAccount>> matrix = new LinkedHashMap<>();

        // Get all staff accounts
        List<StaffAccount> staffAccounts = staffAccountRepository.findAllStaffAccountsByRoleStaff();

        // Iterate over each date in the range
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // Initialize the list for the current date
            matrix.put(date, new ArrayList<>());

            // Iterate over each staff account
            for (StaffAccount staff : staffAccounts) {
                // Get all shifts for the current staff account
                Set<Shift> shifts = staff.getStaffShifts().stream()
                        .map(Staff_Shift::getShift)
                        .collect(Collectors.toSet());

                // Check if the staff has a shift on the current date
                LocalDate finalDate = date;
                if (shifts.stream().anyMatch(shift -> shift.getStartTime().toLocalDate().equals(finalDate))) {
                    // If so, add the staff to the list for the current date
                    matrix.get(date).add(staff);
                }
            }
        }
        return matrix;
    }


    // Method to assign multiple staff to a shift
    @Transactional
    public List<Staff_Shift> assignMultipleStaffToShift(List<Integer> staffIds, int shiftId) {
        List<Staff_Shift> staffShifts = new ArrayList<>();
        for (int staffId : staffIds) {
            Staff_Shift staffShift = assignStaffToShift(staffId, shiftId);
            staffShifts.add(staffShift);
        }
        return staffShifts;
    }

    // Method to assign multiple shifts to a staff
    @Transactional
    public List<Staff_Shift> assignMultipleShiftsToStaff(int staffId, List<Integer> shiftIds) {
        List<Staff_Shift> staffShifts = new ArrayList<>();
        for (int shiftId : shiftIds) {
            Staff_Shift staffShift = assignStaffToShift(staffId, shiftId);
            staffShifts.add(staffShift);
        }
        return staffShifts;
    }

    // Method to remove a staff member from a shift
    @Transactional
    public void removeStaffFromShift(int staffId, long shiftId) {
        // Fetch the staff and shift entities from the database
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        // Find the Staff_Shift entity that links the staff member and the shift
        Staff_Shift staffShift = staffShiftRepository.findByStaffAccountAndShift(staff, shift)
                .orElseThrow(() -> new RuntimeException("Staff is not assigned to this shift"));

        // Delete the Staff_Shift entity from the database
        staffShiftRepository.delete(staffShift);
    }

    // Method to remove a shift from a staff member
    @Transactional
    public void removeShiftFromStaff(int shiftId, int staffId) {
        removeStaffFromShift(staffId, shiftId);
    }
}