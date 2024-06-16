package com.project.JewelryMS.model.ProductSell;

import com.project.JewelryMS.entity.Category;
import com.project.JewelryMS.entity.Promotion;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateProductSellRequest {
    private float carat;
    private long category_id;
    private int chi;
    private String pDescription;
    private String gemstoneType;
    private MultipartFile image;
    private String manufacturer;
    private Float manufactureCost;
    private String metalType;
    private String pName;
    private String productCode;
}
