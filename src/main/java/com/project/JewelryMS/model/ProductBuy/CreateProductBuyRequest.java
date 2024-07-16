package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
