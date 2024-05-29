package com.shop.JewleryMS.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateProductSellRequest {
    Integer categoryID;
    Integer promotionID;
    String name;
    Float cost;
    Boolean status;
    String description;
    String metalType;
    String gemstoneType;
    String productCode;
    String manufacturer;
    Integer productCost;
    String image;
    Integer chi;
    Float carat;

}
