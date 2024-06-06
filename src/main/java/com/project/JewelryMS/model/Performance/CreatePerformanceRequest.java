package com.project.JewelryMS.model.Performance;

import com.project.JewelryMS.entity.StaffAccount;
import lombok.Data;

import java.util.Date;

@Data
public class CreatePerformanceRequest {
    private Date date;
    private int salesMade;
    private double revenueGenerated;
    private int customerSignUp;
    private int staffID;
}
