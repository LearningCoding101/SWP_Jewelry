package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Staff")
@EqualsAndHashCode(exclude = {"staffShifts", "purchaseOrders", "customer", "workArea"})
@ToString(exclude = {"staffShifts", "purchaseOrders", "customer", "workArea"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "account.staffAccount", "shift.staffAccounts"})
public class StaffAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_staffID")
    private int staffID;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_UserID", referencedColumnName = "PK_userID")
    @JsonIgnoreProperties
    private Account account;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "salary")
    private float salary;

    @Column(name = "startDate")
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workAreaId", referencedColumnName = "workAreaID")
    @JsonIgnoreProperties("staffAccounts")
    @JsonManagedReference
    @JsonBackReference
    private WorkArea workArea;

    @OneToMany(mappedBy = "staffAccount", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("staffAccount")
    @JsonManagedReference
    @JsonBackReference
    private Set<Staff_Shift> staffShifts = new HashSet<>();

    @OneToMany(mappedBy = "staffAccountCashier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties
    @JsonBackReference
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @OneToMany(mappedBy = "staffAccount")
    @JsonIgnoreProperties("staffAccount")
    private List<Customer> customer = new ArrayList<>();
}