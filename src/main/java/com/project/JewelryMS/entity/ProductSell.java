package com.project.JewelryMS.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductSell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_productID")
    private long productID;

    @Column(name = "carat")
    private float carat;

    @Column(name = "FK_categoryID")
    private int categoryID;

    @Column(name = "chi")
    private int chi;

    @Column(name = "cost")
    private float cost;

    @Column(name = "pDescription", length = 255)
    private String pDescription;

    @Column(name = "gemstoneType", length = 255)
    private String gemstoneType;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "manufacturer", length = 255)
    private String manufacturer;

    @Column(name = "metalType", length = 255)
    private String metalType;

    @Column(name = "pName", length = 255)
    private String pName;

    @Column(name = "productCode", length = 255)
    private String productCode;

    @Column(name = "productCost")
    private int productCost;

    @Column(name = "FK_promotionID")
    private int promotionID;

    @Column(name = "pStatus")
    private boolean pStatus;
}
