package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import lombok.Data;

import java.sql.Date;
@Data
public class StaffAccountRequest {


    private String phoneNumber;

    private float salary;

    private Date startDate;

    private RoleEnum role;

    private String email;

    private String username;

    private String password;

    private String accountName;


}
