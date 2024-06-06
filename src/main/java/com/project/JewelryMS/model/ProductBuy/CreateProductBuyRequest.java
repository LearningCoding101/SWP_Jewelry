package com.project.JewelryMS.model.ProductBuy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateProductBuyRequest {
    private Long OrderDetailID;
    private Long categoryID;
    private Float appraisalValue;
    private String condition;
    private Float weight;
    private String description;
    private String metalType;
    private String gemstoneType;
    private String productCode;
    private String manufacturer;
    private Integer amount;
    private byte[] image;
    private Integer chi;
    private Integer carat;
}
