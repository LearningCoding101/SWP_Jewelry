package com.project.JewelryMS.model;

import lombok.Data;

import java.util.Date;

@Data
public class CreatePromotionRequest {
    String code;
    String description;
    Date startDate;
    Date endDate;
}
