package com.project.JewelryMS.model.ProductSell;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.Promotion;
import lombok.Data;
import java.util.Base64;
@Data
public class CreateProductSellRequest {
    private float carat;
    private long category_id;
    private int chi;
    private float cost;
    private String pDescription;
    private String gemstoneType;

    private String image;

    private String manufacturer;
    private String metalType;
    private String pName;
    private String productCode;
    private int productCost;
    private boolean pStatus;
}
