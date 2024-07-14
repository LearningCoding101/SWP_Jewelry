package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductBuyRequest {
    private String name;
    private Long category_id;
    private String metalType;
    private String gemstoneType;
    private String image;
    private Float metalWeight;
    private Float gemstoneWeight;
    private Float cost;
}
