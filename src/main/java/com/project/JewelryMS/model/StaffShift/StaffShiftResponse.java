package com.project.JewelryMS.model.StaffShift;

import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.model.Staff.StaffAccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffShiftResponse {
    private int staffID;
    private String accountName;
    private String email;
    private String username;
    private List<StaffAccountResponse.ShiftResponse> shift;

    // Getters and Setters
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShiftResponse {
        private int shiftID;
        private LocalDateTime endTime;
        private int register;
        private String shiftType;
        private LocalDateTime startTime;
        private String status;
        private String workArea;
    }
}
