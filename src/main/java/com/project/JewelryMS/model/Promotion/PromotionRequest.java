package com.project.JewelryMS.model.Promotion;
import lombok.Data;

import java.util.Date;
@Data
public class PromotionRequest {
    long PK_promotionID;
    String code;
    String description;
    Date startDate;
    Date endDate;
    int discount;
}