package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Staff")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "account.staffAccount", "shift.staffAccounts"})
public class StaffAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_staffID")
    private int staffID;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_UserID", referencedColumnName = "PK_userID")
//    @JsonBackReference
    @JsonIgnoreProperties
    private Account account;

//    @ManyToOne
//    @JoinColumn(name = "FK_shiftID", referencedColumnName = "shiftID")
//    @JsonIgnoreProperties
//    private Shift shift;


    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "salary")
    private float salary;

    @Column(name = "startDate")
    private Date startDate;

    @OneToMany(mappedBy = "staffAccount")
    @JsonIgnoreProperties
    Set<Staff_Shift> staffShifts;

}