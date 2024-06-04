package com.project.JewelryMS.model.Staff;

import com.project.JewelryMS.entity.Account;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
@Data
@AllArgsConstructor
public class StaffAccountResponse {
    private int staffID;

    private int shiftID;

    private String phoneNumber;

    private float salary;

    private Date startDate;

    private String Email;

    private String aUsername;

    private String accountName;

    private int status;
}
