package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ProductResponse {
    private long orderDetail_ID;
    private int quantity;
    private long productID;
    private Float carat;
    private Float chi;
    private float cost;
    private String Description;
    private String gemstoneType;
    private String image;
    private String manufacturer;
    private Float manufactureCost;
    private String metalType;
    private String Name;
    private String productCode;
    private boolean Status;
    private long category_id;
    private List<String> promotion_id;
    private int refundQuantity;
}
