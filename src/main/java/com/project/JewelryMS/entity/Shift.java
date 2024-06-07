package com.project.JewelryMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Shift")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shiftID")
    private int shiftID;

    @Column(name = "endTime")
    private Timestamp endTime;

    @Column(name = "register")
    private int register;

    @Column(name = "shiftType")
    private String shiftType;

    @Column(name = "startTime")
    private Timestamp startTime;

    @Column(name = "status")
    private String status;

    @Column(name = "workArea")
    private String workArea;

    @OneToMany(mappedBy = "shift", fetch = FetchType.LAZY)
    private List<StaffAccount> staffAccounts;
}
