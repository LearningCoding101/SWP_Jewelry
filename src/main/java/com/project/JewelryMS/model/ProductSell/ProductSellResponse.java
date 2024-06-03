package com.project.JewelryMS.model.ProductSell;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductSellResponse {
    private long productID;
    private float carat;
    private int chi;
    private float cost;
    private String Description;
    private String gemstoneType;
    private byte[] image;
    private String manufacturer;
    private String metalType;
    private String Name;
    private String productCode;
    private int productCost;
    private boolean Status;
    private long category_id;
    private List<String> promotion_id;
}
