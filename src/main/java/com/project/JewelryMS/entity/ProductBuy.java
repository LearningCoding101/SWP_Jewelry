package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ProductBuy")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyID;

    @OneToOne
    @JoinColumn(name = "FK_OrderDetailID", referencedColumnName = "PK_ODID")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FK_categoryID", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @Column(name = "appraisalValue")
    private Float appraisalValue;

    @Column(name = "pcondition")
    private String pcondition;

    @Column(name = "weight")
    private Float weight;

    @Column(name="description")
    private String description;

    @Column(name = "metalType")
    private String metalType;

    @Column(name = "gemstoneType")
    private String gemstoneType;

    @Column(name = "productCode")
    private String productCode;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "amount")
    private Integer amount;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name= "chi")
    private Integer chi;

    @Column(name = "carat")
    private Integer carat;

    @Column(name = "pbstatus")
    private boolean pbstatus;
}
