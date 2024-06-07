package com.project.JewelryMS.model;

import lombok.Data;

@Data
public class GuaranteeRequest {//For read methods, we pull all datas
    long PK_guaranteeID;

    long FK_productID;
    String policyType;
    String coverage;
}