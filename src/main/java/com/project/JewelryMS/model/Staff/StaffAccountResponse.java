package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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

    private int status;

    //Shifts details
    private List<Shift> shift;
//    private int shiftID;
//
//    private Timestamp startTime;
//
//    private int register;
//
//    private Timestamp endTime;
//
//    private String shiftType;
//
//    private Boolean shiftstatus;
//
//    private String workArea;
}
