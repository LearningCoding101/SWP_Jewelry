package com.project.JewelryMS.model.ProductSell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSellRequest {
    private float carat;
    private long category_id;
    private List<Long> promotion_id;
    private int chi;
    private float cost;
    private String pDescription;
    private String gemstoneType;
    private MultipartFile image;
    private float manufacturer;
    private String metalType;
    private String pName;
    private String productCode;
}
