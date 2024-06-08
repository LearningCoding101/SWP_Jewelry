package com.project.JewelryMS.model.Guarantee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuaranteeResponse {
    long PK_guaranteeID;

    //ProductSell properties
    long productID;


    String policyType;
    String coverage;

    boolean status;
}
