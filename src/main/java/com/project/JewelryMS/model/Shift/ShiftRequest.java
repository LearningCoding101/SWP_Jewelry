package com.project.JewelryMS.model.Shift;

import jakarta.persistence.Column;

import java.sql.Timestamp;

public class ShiftRequest {

    private Timestamp endTime;

    private int register;

    private String shiftType;

    private Timestamp startTime;

    private String status;

    private String workArea;
}
