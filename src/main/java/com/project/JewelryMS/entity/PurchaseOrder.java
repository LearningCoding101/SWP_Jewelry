package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@JsonIgnoreProperties("orderDetails")
public class PurchaseOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long PK_OrderID;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("orderBuyDetails")
    Set<OrderBuyDetail> orderBuyDetails = new HashSet<>();

    String paymentType;
    Float totalAmount;
    Date purchaseDate;
    //1: not pay yet, 2: in process , 3 : paid
    Integer status;
    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "PK_OrderID=" + PK_OrderID +
                ", paymentType='" + paymentType + '\'' +
                ", totalAmount=" + totalAmount +
                ", purchaseDate=" + purchaseDate +
                ", status=" + status +
                '}';
    }
}
