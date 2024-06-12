package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
//import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAccountResponse {
    private int staffID;
    private String phoneNumber;
    private float salary;
    private Date startDate;
    private String accountName;
    private RoleEnum role;
    private int status;
    private String email;
    private String username;
    private List<ShiftResponse> shift;

    // Getters and Setters
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShiftResponse {
        private int shiftID;
        private Date endTime;
        private int register;
        private String shiftType;
        private Date startTime;
        private String status;
        private String workArea;
    }
}
