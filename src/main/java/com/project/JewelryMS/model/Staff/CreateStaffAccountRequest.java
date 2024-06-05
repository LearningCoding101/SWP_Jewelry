package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStaffAccountRequest {
    private int shiftID;
    private String phoneNumber;
    private float salary;
    private Date startDate;
    private long account_id;
}
