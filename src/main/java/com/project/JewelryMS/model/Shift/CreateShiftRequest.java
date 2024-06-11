package com.project.JewelryMS.model.Shift;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class CreateShiftRequest {
    private Date endTime;
    private int register;
    private String shiftType;
    private Date startTime;
    private String status;
    private String workArea;
}
