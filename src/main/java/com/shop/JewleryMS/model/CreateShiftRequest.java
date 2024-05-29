package com.shop.JewleryMS.model;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
public class CreateShiftRequest {
    Timestamp startTime;
    int register;
    Timestamp endTime;
    String shiftType;
    String status;
    String workArea;
}
