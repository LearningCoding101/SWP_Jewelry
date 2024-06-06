package com.project.JewelryMS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Guarantee")
public class Guarantee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long PK_guaranteeID;

    long FK_productID;
    String policyType;
    String coverage;
    boolean status = true;
}
