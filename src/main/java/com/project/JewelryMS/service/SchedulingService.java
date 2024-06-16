package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
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
import java.time.format.DateTimeFormatter;
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

    public Map<String, Map<String, List<StaffShiftResponse>>> getScheduleMatrix(LocalDate startDate, LocalDate endDate) {
        // Define the shift types
        String[] shiftTypes = {"Morning", "Afternoon", "Evening"};

        // Initialize the matrix
        Map<String, Map<String, List<StaffShiftResponse>>> matrix = new LinkedHashMap<>();

        // Get all staff accounts
        List<StaffAccount> staffAccounts = staffAccountRepository.findAllStaffAccountsByRoleStaff();

        // Date and Time formatters
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        // Iterate over each date in the range
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // Initialize the map for the current date
            String formattedDate = date.format(dateFormatter);
            matrix.put(formattedDate, new LinkedHashMap<>());

            // Initialize the list for each shift type
            for (String shiftType : shiftTypes) {
                matrix.get(formattedDate).put(shiftType, new ArrayList<>());
            }

            // Iterate over each staff account
            for (StaffAccount staff : staffAccounts) {
                // Get all shifts for the current staff account
                Set<Shift> shifts = staff.getStaffShifts().stream()
                        .map(Staff_Shift::getShift)
                        .collect(Collectors.toSet());

                // Check if the staff has a shift on the current date
                LocalDate finalDate = date;
                shifts.stream()
                        .filter(shift -> shift.getStartTime().toLocalDate().equals(finalDate))
                        .forEach(shift -> {
                            // Determine the shift type based on the start time
                            int hour = shift.getStartTime().getHour();
                            String shiftType;
                            if (hour >= 7 && hour < 11) {
                                shiftType = "Morning";
                            } else if (hour >= 13 && hour < 16) {
                                shiftType = "Afternoon";
                            } else if (hour >= 17 && hour < 21) {
                                shiftType = "Evening";
                            } else {
                                throw new RuntimeException("Shift does not fall into any defined period");
                            }

                            // Format the start and end times
                            String formattedStartTime = shift.getStartTime().format(timeFormatter);
                            String formattedEndTime = shift.getEndTime().format(timeFormatter);

                            // If so, create a new StaffShiftResponse object and add it to the list for the current date and shift type
                            StaffShiftResponse staffShift = new StaffShiftResponse(staff.getStaffID(), staff.getAccount().getAccountName(), staff.getAccount().getEmail(), staff.getAccount().getUsername(), shifts.stream().map(s -> new StaffShiftResponse.ShiftResponse(s.getShiftID(), formattedEndTime, s.getRegister(), s.getShiftType(), formattedStartTime, s.getStatus(), s.getWorkArea())).collect(Collectors.toList()));
                            matrix.get(formattedDate).get(shiftType).add(staffShift);
                        });
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