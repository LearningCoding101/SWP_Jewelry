package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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
}
