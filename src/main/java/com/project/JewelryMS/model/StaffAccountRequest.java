package com.project.JewelryMS.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Date;
@Data
public class StaffAccountRequest {

    private long staffID;


    private int userID;


    private int shiftID;


    private String phoneNumber;


    private float salary;

    private Date startDate;
}
