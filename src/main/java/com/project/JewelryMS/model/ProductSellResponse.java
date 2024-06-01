package com.project.JewelryMS.model;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.Promotion;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductSellResponse {
    private long productID;
    private float carat;
    private int chi;
    private float cost;
    private String pDescription;
    private String gemstoneType;
    private byte[] image;
    private String manufacturer;
    private String metalType;
    private String pName;
    private String productCode;
    private int productCost;
    private boolean pStatus;
    private long category_id;
}
