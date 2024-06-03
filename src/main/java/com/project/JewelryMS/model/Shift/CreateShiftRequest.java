package com.project.JewelryMS.model.Shift;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class CreateShiftRequest {
    private Timestamp endTime;
    private int register;
    private String shiftType;
    private Timestamp startTime;
    private String status;
    private String workArea;
}
