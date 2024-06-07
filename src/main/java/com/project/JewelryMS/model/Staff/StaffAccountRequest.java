package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.entity.Account;
import lombok.Data;

import java.sql.Date;
@Data
public class StaffAccountRequest {

    private long staffID;

    private int shiftID;

    private String phoneNumber;

    private float salary;

    private Date startDate;
}
