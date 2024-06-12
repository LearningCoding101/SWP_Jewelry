package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductBuyRequest {
    private String pbName;
    private String categoryName;
    private String metalType;
    private String gemstoneType;
    private MultipartFile image;
    private Integer chi;
    private Integer carat;
}
