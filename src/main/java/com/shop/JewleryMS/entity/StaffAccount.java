package com.shop.JewleryMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Staff")
public class StaffAccount{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PK_staffID")
    int staffID;
    @Column(name="FK_UserID")
    int userID;
    @Column(name="FK_shiftID")
    int shiftID;
    @Column(name="startDate")
    Date startDate;
    @Column(name="phoneNumber")
    String phoneNumber;
    @Column(name="salary")
    float salary;
}
