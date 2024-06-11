package com.project.JewelryMS.model.Shift;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftRequest {
    private int shiftID;
    private Date endTime;
    private int register;
    private String shiftType;
    private Date startTime;
    private String status;
    private String workArea;
}
