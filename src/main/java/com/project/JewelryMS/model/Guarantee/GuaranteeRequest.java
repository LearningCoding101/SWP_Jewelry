package com.project.JewelryMS.model.Guarantee;

import lombok.Data;

@Data
public class GuaranteeRequest {
    long PK_guaranteeID;

    long FK_productID;
    String policyType;
    String coverage;
}