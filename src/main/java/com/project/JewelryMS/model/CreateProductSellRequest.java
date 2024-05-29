package com.project.JewelryMS.model;

import lombok.Data;

@Data
public class CreateProductSellRequest {
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
