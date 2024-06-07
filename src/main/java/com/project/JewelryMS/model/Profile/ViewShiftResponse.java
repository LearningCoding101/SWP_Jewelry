package com.project.JewelryMS.model.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewShiftResponse {
    String email;
    String username;
    String phone;
    int shiftID;
    Timestamp startTime;
    int register;
    Timestamp endTime;
    String shiftType;
    String status;
    String workArea;
}
