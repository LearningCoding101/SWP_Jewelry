package com.project.JewelryMS.model.Guarantee;

import lombok.Data;

@Data
public class GuaranteeResponse {
    long PK_guaranteeID;

    //ProductSell properties
    long FK_productID;


    String policyType;
    String coverage;

    boolean status;
}
