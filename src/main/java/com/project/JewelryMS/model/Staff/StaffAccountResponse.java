package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAccountResponse {
    private int staffID;

    private String phoneNumber;

    private float salary;

    private Date startDate;

    private String Email;

    private String aUsername;

    private String accountName;

    private RoleEnum role;

    private int shiftID;

    private Timestamp startTime;

    private int register;

    private Timestamp endTime;

    private String shiftType;

    private String shiftstatus;

    private String workArea;
}
