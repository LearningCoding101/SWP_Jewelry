package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.repository.StaffShiftRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class SchedulingService {

    @Autowired
    private StaffAccountRepository staffAccountRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private StaffShiftRepository staffShiftRepository;

    @Autowired
    private ShiftService shiftService;

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

                // Get all shifts of the current type for the current date
                List<Shift> shifts = shiftRepository.findAllByDateAndType(date, shiftType);

                // For each shift, get the staff assigned to it and create a StaffShiftResponse
                for (Shift shift : shifts) {
                    List<StaffAccount> staffAccounts = staffAccountRepository.findAllByShift(shift);

                    // Format the start and end times
                    String formattedStartTime = shift.getStartTime().format(timeFormatter);
                    String formattedEndTime = shift.getEndTime().format(timeFormatter);

                    // Create a new StaffShiftResponse object and add it to the list for the current date and shift type
                    StaffShiftResponse staffShift = new StaffShiftResponse(shift.getShiftID(), formattedStartTime, formattedEndTime, shiftType, shift.getStatus(), shift.getWorkArea(), shift.getRegister(), staffAccounts.stream().map(s -> new StaffShiftResponse.StaffResponse(s.getStaffID(), s.getAccount().getAccountName(), s.getAccount().getEmail(), s.getAccount().getUsername())).collect(Collectors.toList()));
                    matrix.get(formattedDate).get(shiftType).add(staffShift);
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

    @Transactional
    public StaffShiftResponse assignStaffToDay(int staffId, LocalDate date, String shiftType) {
        // Fetch the staff entity from the database
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new ShiftAssignmentException("Staff not found"));

        // Check if the staff member is already assigned to a shift during the same period on the same day
        boolean isAssigned = staff.getStaffShifts().stream()
                .anyMatch(ss -> ss.getShift().getStartTime().toLocalDate().equals(date) && ss.getShift().getShiftType().equals(shiftType));

        if (isAssigned) {
            throw new ShiftAssignmentException("Staff is already assigned to a shift during this period on this day. Please choose a different shift or staff member.");
        }
        // Find a shift on the specified date and period
        Shift shift = shiftRepository.findAllByDateAndType(date, shiftType)
                .stream().findFirst().orElse(null);

        // If no shift exists, create a new one using the ShiftService
        if (shift == null) {
            CreateShiftRequest createShiftRequest = new CreateShiftRequest();
            createShiftRequest.setShiftType(shiftType);
            createShiftRequest.setStatus("Active");
            createShiftRequest.setWorkArea("Sales");  // Set this according to your requirements
            createShiftRequest.setRegister(0);

            // Define the start and end times for each shift type
            String startTime, endTime;
            endTime = switch (shiftType) {
                case "Morning" -> {
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 08";
                    yield date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 12";
                }
                case "Afternoon" -> {
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 13";
                    yield date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 16";
                }
                case "Evening" -> {
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 17";
                    yield date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 21";
                }
                default -> throw new RuntimeException("Invalid shift type");
            };

            createShiftRequest.setStartTime(startTime);
            createShiftRequest.setEndTime(endTime);

            ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
            shift = shiftRepository.findById((long) createdShift.getShiftID())
                    .orElseThrow(() -> new RuntimeException("Shift not found"));
        }

        // Create a new Staff_Shift entity
        Staff_Shift staffShift = new Staff_Shift();
        staffShift.setStaffAccount(staff);
        staffShift.setShift(shift);

        // Save the new Staff_Shift entity to the database
        staffShift = staffShiftRepository.save(staffShift);

        // Convert the new Staff_Shift entity to a StaffShiftResponse and return it
        return toStaffShiftResponse(staffShift);
    }

    // Helper method to convert a Staff_Shift entity to a StaffShiftResponse
    private StaffShiftResponse toStaffShiftResponse(Staff_Shift staffShift) {
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh a");
        String formattedStartTime = staffShift.getShift().getStartTime().format(timeFormatter);
        String formattedEndTime = staffShift.getShift().getEndTime().format(timeFormatter);

        StaffShiftResponse.StaffResponse staffResponse = new StaffShiftResponse.StaffResponse(
                staffShift.getStaffAccount().getStaffID(),
                staffShift.getStaffAccount().getAccount().getAccountName(),
                staffShift.getStaffAccount().getAccount().getEmail(),
                staffShift.getStaffAccount().getAccount().getUsername()
        );

        return new StaffShiftResponse(
                staffShift.getShift().getShiftID(),
                formattedStartTime,
                formattedEndTime,
                staffShift.getShift().getShiftType(),
                staffShift.getShift().getStatus(),
                staffShift.getShift().getWorkArea(),
                staffShift.getShift().getRegister(),
                Collections.singletonList(staffResponse)
        );
    }

    // Custom exception class
    private static class ShiftAssignmentException extends RuntimeException {
        public ShiftAssignmentException(String message) {
            super(message);
        }
    }

    @Transactional
    public Shift updateShiftWorkArea(long shiftId, String newWorkArea) {
        // Fetch the shift entity from the database
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        // Update the work area
        shift.setWorkArea(newWorkArea);

        // Save the updated shift entity to the database
        return shiftRepository.save(shift);
    }

    @Transactional
    public List<StaffShiftResponse> assignStaffToDateRange(List<Integer> staffIds, LocalDate startDate, LocalDate endDate, List<String> shiftTypes) {
        List<StaffShiftResponse> staffShiftResponses = new ArrayList<>();
        // Iterate over each date in the range, including the start and end dates
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (String shiftType : shiftTypes) {
                for (int staffId : staffIds) {
                    StaffShiftResponse response = tryAssignStaffToDay(staffId, date, shiftType);
                    if (response != null) {
                        staffShiftResponses.add(response);
                    }
                }
            }
        }
        return staffShiftResponses;
    }

    @Transactional
    public StaffShiftResponse tryAssignStaffToDay(int staffId, LocalDate date, String shiftType) {
        // Fetch the staff entity from the database
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new ShiftAssignmentException("Staff not found"));

        // Check if the staff member is already assigned to a shift during the same period on the same day
        boolean isAssigned = staff.getStaffShifts().stream()
                .anyMatch(ss -> ss.getShift().getStartTime().toLocalDate().equals(date) && ss.getShift().getShiftType().equals(shiftType));

        if (isAssigned) {
            return null;  // Skip the assignment
        }
        // Find a shift on the specified date and period
        Shift shift = shiftRepository.findAllByDateAndType(date, shiftType)
                .stream().findFirst().orElse(null);

        // If no shift exists, create a new one using the ShiftService
        if (shift == null) {
            CreateShiftRequest createShiftRequest = new CreateShiftRequest();
            createShiftRequest.setShiftType(shiftType);
            createShiftRequest.setStatus("Active");
            createShiftRequest.setWorkArea("Sales");  // Set this according to your requirements
            createShiftRequest.setRegister(0);

            // Define the start and end times for each shift type
            String startTime, endTime;
            endTime = switch (shiftType) {
                case "Morning" -> {
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 08";
                    yield date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 12";
                }
                case "Afternoon" -> {
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 13";
                    yield date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 17";
                }
                case "Evening" -> {
                    startTime = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 17";
                    yield date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 21";
                }
                default -> throw new RuntimeException("Invalid shift type");
            };

            createShiftRequest.setStartTime(startTime);
            createShiftRequest.setEndTime(endTime);

            ShiftRequest createdShift = shiftService.createShift(createShiftRequest);
            shift = shiftRepository.findById((long) createdShift.getShiftID())
                    .orElseThrow(() -> new RuntimeException("Shift not found"));
        }

        // Create a new Staff_Shift entity
        Staff_Shift staffShift = new Staff_Shift();
        staffShift.setStaffAccount(staff);
        staffShift.setShift(shift);

        // Save the new Staff_Shift entity to the database
        staffShift = staffShiftRepository.save(staffShift);

        // Convert the new Staff_Shift entity to a StaffShiftResponse and return it
        return toStaffShiftResponse(staffShift);
    }

    //This can be used again, if necessary
//    @Transactional
//    public List<StaffShiftResponse> assignStaffByDayOfWeek(
//            Map<Integer, Map<DayOfWeek, List<String>>> staffAvailability,
//            LocalDate startDate,
//            LocalDate endDate) {
//
//        List<StaffShiftResponse> staffShiftResponses = new ArrayList<>();
//
//        // Iterate over each date in the range
//        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//            DayOfWeek currentDayOfWeek = date.getDayOfWeek();
//
//            for (Map.Entry<Integer, Map<DayOfWeek, List<String>>> entry : staffAvailability.entrySet()) {
//                int staffId = entry.getKey();
//                Map<DayOfWeek, List<String>> availability = entry.getValue();
//
//                // Check if the current day of the week is in the staff's availability
//                if (availability.containsKey(currentDayOfWeek)) {
//                    List<String> shiftTypes = availability.get(currentDayOfWeek);
//
//                    for (String shiftType : shiftTypes) {
//                        try {
//                            StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
//                            staffShiftResponses.add(response);
//                        } catch (ShiftAssignmentException e) {
//                            // Log and continue if staff is already assigned
//                            System.out.println("Staff ID " + staffId + " is already assigned on " + date + " for " + shiftType + " shift.");
//                        }
//                    }
//                }
//            }
//        }
//        return staffShiftResponses;
//    }
    @Transactional
    public void removeStaffFromShiftsInRange(int staffId, LocalDate startDate, LocalDate endDate) {
        // Validate staff existence
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        // Retrieve all shifts for the staff within the date range
        List<Staff_Shift> staffShifts = staffShiftRepository.findAllByStaffAccountAndShift_StartTimeBetween(
                staff,
                startDate.atStartOfDay(), // Start of day
                endDate.plusDays(1).atStartOfDay().minusNanos(1) // End of day
        );

        // Remove each staff shift found
        staffShiftRepository.deleteAll(staffShifts);
    }

//ASSIGN STAFF BY THEIR SHIFT TYPE
    @Transactional
    public List<StaffShiftResponse> assignStaffByShiftTypePattern(
            Map<Integer, List<List<String>>> staffShiftPatterns, LocalDate startDate, LocalDate endDate) {

        List<StaffShiftResponse> staffShiftResponses = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            for (Map.Entry<Integer, List<List<String>>> entry : staffShiftPatterns.entrySet()) {
                int staffId = entry.getKey();
                List<List<String>> shiftPatterns = entry.getValue();

                if (shiftPatterns.size() == 1) {
                    // Single shift type: Work all weekdays (Monday to Saturday)
                    if (!isSunday(dayOfWeek)) {
                        assignShiftForDay(staffShiftResponses, staffId, date, shiftPatterns.get(0));
                    }
                } else if (shiftPatterns.size() == 2) {
                    // Two shift types: Alternate days based on the current day of the week
                    if (shouldWorkTodayForTwoShiftType(today, dayOfWeek)) {
                        assignShiftForDay(staffShiftResponses, staffId, date, shiftPatterns.get(0));
                        assignShiftForDay(staffShiftResponses, staffId, date, shiftPatterns.get(1));
                    }
                } else if (shiftPatterns.size() == 3) {
                    // Three shift types: Work two days a week
                    if (shouldWorkTodayForThreeShiftType(today, dayOfWeek)) {
                        assignShiftForDay(staffShiftResponses, staffId, date, shiftPatterns.get(0));
                        assignShiftForDay(staffShiftResponses, staffId, date, shiftPatterns.get(1));
                        assignShiftForDay(staffShiftResponses, staffId, date, shiftPatterns.get(2));
                    }
                }
            }
        }

        return staffShiftResponses;
    }

    private boolean isMWF(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.FRIDAY;
    }

    private boolean isTTS(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.TUESDAY || dayOfWeek == DayOfWeek.THURSDAY || dayOfWeek == DayOfWeek.SATURDAY;
    }

    private boolean isSunday(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean shouldWorkTodayForTwoShiftType(LocalDate today, DayOfWeek dayOfWeek) {
        boolean isTodayEven = today.getDayOfWeek().getValue() % 2 == 0;
        boolean isMWFPattern = isMWF(dayOfWeek);
        boolean isTTSPattern = isTTS(dayOfWeek);

        if (isTodayEven) {
            return isTTSPattern;
        } else {
            return isMWFPattern;
        }
    }

    private boolean shouldWorkTodayForThreeShiftType(LocalDate today, DayOfWeek dayOfWeek) {
        DayOfWeek todayDayOfWeek = today.getDayOfWeek();

        if (todayDayOfWeek == DayOfWeek.MONDAY || todayDayOfWeek == DayOfWeek.THURSDAY) {
            return dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.THURSDAY;
        } else if (todayDayOfWeek == DayOfWeek.TUESDAY || todayDayOfWeek == DayOfWeek.FRIDAY) {
            return dayOfWeek == DayOfWeek.TUESDAY || dayOfWeek == DayOfWeek.FRIDAY;
        } else if (todayDayOfWeek == DayOfWeek.WEDNESDAY || todayDayOfWeek == DayOfWeek.SATURDAY) {
            return dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.SATURDAY;
        }
        return false;
    }

    private void assignShiftForDay(List<StaffShiftResponse> responses, int staffId, LocalDate date, List<String> shiftTypes) {
        for (String shiftType : shiftTypes) {
            try {
                StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
                responses.add(response);
            } catch (ShiftAssignmentException e) {
                System.out.println("Staff ID " + staffId + " is already assigned on " + date + " for " + shiftType + " shift.");
            }
        }
    }

}
