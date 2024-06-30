package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long PK_ODID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_ProductID", referencedColumnName = "PK_ProductID")
    ProductSell productSell;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_OrderID", referencedColumnName = "PK_OrderID")
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    PurchaseOrder purchaseOrder;

    Integer quantity;

    @Column(name = "guaranteeEndDate")
    Timestamp guaranteeEndDate;

    @Column(name = "discountCode")
    String discountCode;

}
