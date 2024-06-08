package com.project.JewelryMS.model.ProductSell;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.Promotion;
import lombok.Data;

@Data
public class CreateProductSellRequest {
    private float carat;
    private long category_id;
    private int chi;
    private String pDescription;
    private String gemstoneType;
    private String image;
    private float manufacturer;
    private String metalType;
    private String pName;
    private String productCode;
}
