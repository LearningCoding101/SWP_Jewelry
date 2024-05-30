package com.project.JewelryMS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Staff")
public class StaffAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_staffID")
    private int staffID;

    @Column(name = "FK_UserID")
    private int userID;

    @Column(name = "FK_shiftID")
    private int shiftID;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "salary")
    private float salary;

    @Column(name = "startDate")
    private Date startDate;

}