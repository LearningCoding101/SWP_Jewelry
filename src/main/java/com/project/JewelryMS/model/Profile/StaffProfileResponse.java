package com.project.JewelryMS.model.Profile;

import com.project.JewelryMS.enumClass.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffProfileResponse {
    private RoleEnum role;
    private String email;
    private String username;
    private String accountName;
    private LocalDate startDate;
    private String phoneNumber;
    private float salary;
    private int status;

}
