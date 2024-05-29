package com.project.JewelryMS.model;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class ProductSellRequest {
    private long productID;
    private float carat;
    private int categoryID;
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
    private int promotionID;
    private boolean pStatus;
}
