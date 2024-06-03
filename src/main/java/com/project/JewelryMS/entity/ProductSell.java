package com.project.JewelryMS.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "category.productSell", "promotion.productSell"})
public class ProductSell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_productID")
    private long productID;

    @Column(name = "carat")
    private float carat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_categoryID", referencedColumnName = "id")
//    @JsonBackReference
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ProductSell_Promotion",
            joinColumns = @JoinColumn(name = "productsellID", referencedColumnName = "PK_productID"),
            inverseJoinColumns = @JoinColumn(name = "promotionID", referencedColumnName = "PK_promotionID")
    )
    @JsonIgnoreProperties("productSell")
    private List<Promotion> promotion ;

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

    @Column(name = "pStatus")
    private boolean pStatus;
}
