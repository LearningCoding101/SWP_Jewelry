package com.project.JewelryMS.model;

import lombok.Data;

@Data
public class CreateGuaranteeRequest {//For read methods, we pull all datas
    long FK_productID;
    String policyType;
    String coverage;
}