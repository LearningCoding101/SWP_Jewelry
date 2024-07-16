package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBuyResponse {
    Long ProductBuyID;
    String categoryName;
    String pbName;
    String metalType;
    Float chi;
    String gemstoneType;
    Float carat;
    String image;
    Float cost;
    Boolean pbStatus;
}
