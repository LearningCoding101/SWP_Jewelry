package com.project.JewelryMS.model.Promotion;

import lombok.Data;

import java.util.Date;

@Data
public class CreatePromotionRequest {
    String code;
    String description;
    Date startDate;
    Date endDate;
}
