package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignStaffByDayOfWeekRequest {
    private List<Integer> staffIds;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> shiftTypes;
    private List<String> daysOfWeek; // e.g., "Monday", "Wednesday", "Friday"

}
