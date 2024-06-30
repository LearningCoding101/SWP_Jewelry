package com.project.JewelryMS.service;

import com.project.JewelryMS.entity.Shift;
import com.project.JewelryMS.entity.StaffAccount;
import com.project.JewelryMS.entity.Staff_Shift;
import com.project.JewelryMS.model.Shift.CreateShiftRequest;
import com.project.JewelryMS.model.Shift.ShiftRequest;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import com.project.JewelryMS.model.StaffShift.StaffShiftResponse;
import com.project.JewelryMS.repository.ShiftRepository;
import com.project.JewelryMS.repository.StaffAccountRepository;
import com.project.JewelryMS.repository.StaffShiftRepository;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class SchedulingService {

    private static final int THREAD_POOL_SIZE = 30;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

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
    //Old version
    public Map<String, Map<String, List<StaffShiftResponse>>> getScheduleMatrix(LocalDate startDate, LocalDate endDate) {
        // Define the shift types
        String[] shiftTypes = {"Morning", "Afternoon", "Evening"};

        // Initialize the matrix
        Map<String, Map<String, List<StaffShiftResponse>>> matrix = new ConcurrentHashMap<>();

        // Date and Time formatters
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<>();

        // Iterate over each date in the range
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // Format the current date
            String formattedDate = date.format(dateFormatter);
            matrix.putIfAbsent(formattedDate, new ConcurrentHashMap<>());

            // Process each shift type concurrently
            for (String shiftType : shiftTypes) {
                LocalDate finalDate = date;
                futures.add(executorService.submit(() -> {
                    // Fetch all shifts for the current date and shift type
                    List<Shift> shifts = shiftRepository.findAllByDateAndType(finalDate, shiftType);
                    List<StaffShiftResponse> shiftResponses = new CopyOnWriteArrayList<>();

                    for (Shift shift : shifts) {
                        // Fetch all staff accounts for the current shift
                        List<StaffAccount> staffAccounts = staffAccountRepository.findAllByShift(shift);
                        // Format the start and end times
                        String formattedStartTime = shift.getStartTime().format(timeFormatter);
                        String formattedEndTime = shift.getEndTime().format(timeFormatter);
                        // Create a StaffShiftResponse object
                        StaffShiftResponse staffShift = new StaffShiftResponse(
                                shift.getShiftID(),
                                formattedStartTime,
                                formattedEndTime,
                                shiftType,
                                shift.getStatus(),
                                shift.getWorkArea(),
                                shift.getRegister(),
                                staffAccounts.stream()
                                        .map(s -> new StaffShiftResponse.StaffResponse(
                                                s.getStaffID(),
                                                s.getAccount().getAccountName(),
                                                s.getAccount().getEmail(),
                                                s.getAccount().getUsername()))
                                        .collect(Collectors.toList())
                        );
                        // Add the StaffShiftResponse to the list
                        shiftResponses.add(staffShift);
                    }
                    // Add the shift responses to the matrix
                    matrix.get(formattedDate).put(shiftType, shiftResponses);
                }));
            }
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();  // Handle exception
            }
        }
        // Shutdown the executor service
        executorService.shutdown();
        return matrix;
    }

//    // Method to clean up shifts based on criteria (no staff assigned and older than 3 months shifts)
//    public void cleanUpShifts() {
//        LocalDate twoMonthsAgo = LocalDate.now().minusMonths(3);
//
//        // Find shifts with no staff assigned
//        List<Shift> shiftsWithoutStaff = shiftRepository.findShiftsWithoutStaff();
//
//        // Find shifts older than 2 months
//        List<Shift> shiftsOlderThanTwoMonths = shiftRepository.findShiftsOlderThan(twoMonthsAgo);
//
//        // Combine both lists to delete shifts that meet either criteria
//        List<Shift> shiftsToDelete = new ArrayList<>();
//        shiftsToDelete.addAll(shiftsWithoutStaff);
//        shiftsToDelete.addAll(shiftsOlderThanTwoMonths);
//
//        for (Shift shift : shiftsToDelete) {
//            shiftRepository.delete(shift);
//        }
//    }
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
        StaffAccount staff = staffAccountRepository.findById(staffId)
                .orElseThrow(() -> new ShiftAssignmentException("Staff not found"));

        // Explicitly initialize the collection
        Hibernate.initialize(staff.getStaffShifts());

        boolean isAssigned = staff.getStaffShifts().stream()
                .anyMatch(ss -> ss.getShift().getStartTime().toLocalDate().equals(date) && ss.getShift().getShiftType().equals(shiftType));

        if (isAssigned) {
            throw new ShiftAssignmentException("Staff is already assigned to a shift during this period on this day. Please choose a different shift or staff member.");
        }

        Shift shift = shiftRepository.findAllByDateAndType(date, shiftType)
                .stream().findFirst().orElse(null);

        if (shift == null) {
            CreateShiftRequest createShiftRequest = new CreateShiftRequest();
            createShiftRequest.setShiftType(shiftType);
            createShiftRequest.setStatus("Active");
            createShiftRequest.setWorkArea("Sales");
            createShiftRequest.setRegister(0);

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

        Staff_Shift staffShift = new Staff_Shift();
        staffShift.setStaffAccount(staff);
        staffShift.setShift(shift);

        staffShift = staffShiftRepository.save(staffShift);

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
        List<StaffShiftResponse> staffShiftResponses = new CopyOnWriteArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1)).toList();

        List<Future<?>> futures = new ArrayList<>();
        for (LocalDate date : dates) {
            for (String shiftType : shiftTypes) {
                for (int staffId : staffIds) {
                    futures.add(executorService.submit(() -> {
                        StaffShiftResponse response = tryAssignStaffToDay(staffId, date, shiftType);
                        if (response != null) {
                            staffShiftResponses.add(response);
                        }
                    }));
                }
            }
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();  // Handle exception
            }
        }

        executorService.shutdown();
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

    //Original shift type pattern, unused
//    @Transactional
//    public List<StaffShiftResponse> assignStaffByShiftTypePattern(
//            Map<String, List<Integer>> staffShiftPatterns, LocalDate startDate, LocalDate endDate) {
//
//        List<StaffShiftResponse> staffShiftResponses = new CopyOnWriteArrayList<>();
//        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
//        List<LocalDate> dates = IntStream.range(0, totalDays)
//                .mapToObj(startDate::plusDays)
//                .filter(date -> date.getDayOfWeek() != DayOfWeek.SUNDAY)
//                .toList();
//
//        // Create a map to track how often each staff ID appears
//        Map<Integer, Integer> staffFrequency = new HashMap<>();
//        for (List<Integer> staffIds : staffShiftPatterns.values()) {
//            for (int staffId : staffIds) {
//                staffFrequency.put(staffId, staffFrequency.getOrDefault(staffId, 0) + 1);
//            }
//        }
//
//        List<Future<?>> futures = new ArrayList<>();
//
//        for (Map.Entry<String, List<Integer>> entry : staffShiftPatterns.entrySet()) {
//            String shiftType = entry.getKey();
//            List<Integer> staffIds = entry.getValue();
//
//            for (int staffId : staffIds) {
//                int frequency = staffFrequency.get(staffId);
//                for (int i = 0; i < dates.size(); i += frequency) {
//                    LocalDate date = dates.get(i);
//                    futures.add(executorService.submit(() -> {
//                        try {
//                            StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
//                            staffShiftResponses.add(response);
//                        } catch (ShiftAssignmentException e) {
//                            System.out.println("Staff ID " + staffId + " is already assigned on " + date + " for " + shiftType + " shift.");
//                        }
//                    }));
//                }
//            }
//        }
//
//        // Wait for all tasks to complete
//        for (Future<?> future : futures) {
//            try {
//                future.get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();  // Handle exception
//            }
//        }
//
//        return staffShiftResponses;
//    }

    //A modified version of the shift Type pattern, modified by demand by the front end team
    @Transactional
    public List<StaffAccountResponse> assignStaffByShiftTypePattern(
            Map<String, List<Integer>> staffShiftPatterns, LocalDate startDate, LocalDate endDate) {

        List<StaffShiftResponse> staffShiftResponses = new CopyOnWriteArrayList<>();
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        List<LocalDate> dates = IntStream.range(0, totalDays)
                .mapToObj(startDate::plusDays)
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .toList();

        // Create a map to track how often each staff ID appears
        Map<Integer, Integer> staffFrequency = new HashMap<>();
        for (List<Integer> staffIds : staffShiftPatterns.values()) {
            for (int staffId : staffIds) {
                staffFrequency.put(staffId, staffFrequency.getOrDefault(staffId, 0) + 1);
            }
        }

        List<Future<?>> futures = new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry : staffShiftPatterns.entrySet()) {
            String shiftType = entry.getKey();
            List<Integer> staffIds = entry.getValue();

            for (int staffId : staffIds) {
                int frequency = staffFrequency.get(staffId);
                for (int i = 0; i < dates.size(); i += frequency) {
                    LocalDate date = dates.get(i);
                    futures.add(executorService.submit(() -> {
                        try {
                            StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
                            staffShiftResponses.add(response);
                        } catch (ShiftAssignmentException e) {
                            System.out.println("Staff ID " + staffId + " is already assigned on " + date + " for " + shiftType + " shift.");
                        }
                    }));
                }
            }
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();  // Handle exception
            }
        }

        // Collect unique staff IDs from the responses
        Set<Integer> staffIds = staffShiftResponses.stream()
                .map(StaffShiftResponse::getStaff)
                .flatMap(List::stream)
                .map(StaffShiftResponse.StaffResponse::getStaffID)
                .collect(Collectors.toSet());

        // Fetch staff accounts and map to responses
        List<StaffAccount> staffAccounts = staffAccountRepository.findAllById(staffIds);
        return staffAccounts.stream()
                .map(this::mapToStaffAccountResponse)
                .collect(Collectors.toList());
    }

    // Method to map StaffAccount to StaffAccountResponse
    private StaffAccountResponse mapToStaffAccountResponse(StaffAccount staffAccount) {
        return new StaffAccountResponse(
                staffAccount.getStaffID(),
                staffAccount.getPhoneNumber(),
                staffAccount.getSalary(),
                staffAccount.getStartDate(),
                staffAccount.getAccount().getAccountName(),
                staffAccount.getAccount().getRole(),
                staffAccount.getAccount().getStatus(),
                staffAccount.getAccount().getEmail(),
                staffAccount.getAccount().getUsername(),
                staffAccount.getStaffShifts().stream()
                        .map(shift -> new StaffAccountResponse.ShiftResponse(
                                shift.getShift().getShiftID(),
                                shift.getShift().getEndTime(),
                                shift.getShift().getRegister(),
                                shift.getShift().getShiftType(),
                                shift.getShift().getStartTime(),
                                shift.getShift().getStatus(),
                                shift.getShift().getWorkArea()
                        ))
                        .collect(Collectors.toList())
        );
    }


    //New method for fast assigning staff
//    Staff with staffID % 3 == 1 get single shifts (morning, afternoon, or evening).
//    Staff with staffID % 3 == 2 get double shifts (morning-afternoon).
//    Staff with staffID % 3 == 0 get double shifts (afternoon-evening).
//    Staff with staffId % 3 == 2 will be assigned shifts on Monday, Wednesday, and Friday.
//    Staff with staffId % 3 == 0 will be assigned shifts on Tuesday, Thursday, and Saturday.

//    If the list contains all staff types (staffId % 3 == 0, 1, 2),
//      the schedule will be filled for the given period except for Sundays.
    @Transactional
    public List<StaffAccountResponse> assignRandomStaffShiftPattern(
            List<Integer> staffIds, LocalDate startDate, LocalDate endDate) {

        List<StaffShiftResponse> staffShiftResponses = new CopyOnWriteArrayList<>();
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        List<LocalDate> dates = IntStream.range(0, totalDays)
                .mapToObj(startDate::plusDays)
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .toList();

        List<Integer> mod0Staff = staffIds.stream().filter(id -> id % 3 == 0).toList();
        List<Integer> mod1Staff = staffIds.stream().filter(id -> id % 3 == 1).toList();
        List<Integer> mod2Staff = staffIds.stream().filter(id -> id % 3 == 2).toList();

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<>();

        for (LocalDate date : dates) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.FRIDAY) {
                for (int staffId : mod2Staff) {
                    futures.add(assignShift(executorService, staffId, date, "Morning", staffShiftResponses));
                    futures.add(assignShift(executorService, staffId, date, "Afternoon", staffShiftResponses));
                }
            } else if (dayOfWeek == DayOfWeek.TUESDAY || dayOfWeek == DayOfWeek.THURSDAY || dayOfWeek == DayOfWeek.SATURDAY) {
                for (int staffId : mod0Staff) {
                    futures.add(assignShift(executorService, staffId, date, "Afternoon", staffShiftResponses));
                    futures.add(assignShift(executorService, staffId, date, "Evening", staffShiftResponses));
                }
            }

            for (int staffId : mod1Staff) {
                futures.add(assignSingleShift(executorService, staffId, date, staffShiftResponses));
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();

        Set<Integer> uniqueStaffIds = staffShiftResponses.stream()
                .map(StaffShiftResponse::getStaff)
                .flatMap(List::stream)
                .map(StaffShiftResponse.StaffResponse::getStaffID)
                .collect(Collectors.toSet());

        List<StaffAccount> staffAccounts = staffAccountRepository.findAllById(uniqueStaffIds);
        return staffAccounts.stream()
                .map(this::mapToStaffAccountResponse)
                .collect(Collectors.toList());
    }

    private Future<?> assignShift(ExecutorService executorService, int staffId, LocalDate date, String shiftType, List<StaffShiftResponse> staffShiftResponses) {
        return executorService.submit(() -> {
            try {
                StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
                synchronized (staffShiftResponses) {
                    staffShiftResponses.add(response);
                }
            } catch (ShiftAssignmentException e) {
                System.out.println("Staff ID " + staffId + " is already assigned on " + date + ".");
            }
        });
    }

    private Future<?> assignSingleShift(ExecutorService executorService, int staffId, LocalDate date, List<StaffShiftResponse> staffShiftResponses) {
        return executorService.submit(() -> {
            try {
                String shiftType = getRandomSingleShiftType();
                StaffShiftResponse response = assignStaffToDay(staffId, date, shiftType);
                synchronized (staffShiftResponses) {
                    staffShiftResponses.add(response);
                }
            } catch (ShiftAssignmentException e) {
                System.out.println("Staff ID " + staffId + " is already assigned on " + date + ".");
            }
        });
    }

    private String getRandomSingleShiftType() {
        List<String> singleShiftTypes = Arrays.asList("Morning", "Afternoon", "Evening");
        Collections.shuffle(singleShiftTypes);
        return singleShiftTypes.get(0);
    }

}