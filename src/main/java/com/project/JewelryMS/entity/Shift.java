package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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
    private String endTime;

    @Column(name = "register")
    private int register;

    @Column(name = "shiftType")
    private String shiftType;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "status")
    private String status;

    @Column(name = "workArea")
    private String workArea;

    @OneToMany(mappedBy = "shift", fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<Staff_Shift> staffShifts;
}
