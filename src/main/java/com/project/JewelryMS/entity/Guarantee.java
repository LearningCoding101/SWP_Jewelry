package com.project.JewelryMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Guarantee")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "productSell.guarantee"})
public class Guarantee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long PK_guaranteeID;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_productID", referencedColumnName = "PK_productID")
    @JsonIgnoreProperties
    private ProductSell productSell;

    //For years, or months
    String policyType;

    //For what product
    String coverage;
    boolean status = true;
}
