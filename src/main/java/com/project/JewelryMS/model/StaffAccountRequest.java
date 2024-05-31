package com.project.JewelryMS.model;

import com.project.JewelryMS.entity.Account;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Date;
@Data
public class StaffAccountRequest {

    private long staffID;

    private int shiftID;


    private String phoneNumber;


    private float salary;

    private Date startDate;
    private Account account;
}
