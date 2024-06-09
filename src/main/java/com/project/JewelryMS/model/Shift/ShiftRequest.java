package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftRequest {
    private int shiftID;
    private Timestamp endTime;
    private int register;
    private String shiftType;
    private Timestamp startTime;
    private Boolean status;
    private String workArea;
}
