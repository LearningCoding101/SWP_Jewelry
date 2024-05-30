package com.project.JewelryMS.model;

import lombok.Data;

import java.sql.Date;
@Data
public class CreateStaffAccountRequest {
    private int userID;

    private int shiftID;


    private String phoneNumber;


    private float salary;

    private Date startDate;
}
