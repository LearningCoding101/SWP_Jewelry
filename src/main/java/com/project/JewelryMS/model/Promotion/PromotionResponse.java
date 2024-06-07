package com.project.JewelryMS.model.Promotion;

import lombok.*;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponse {
    long promotionID;
    String code;
    String description;
    Date startDate;
    Date endDate;
    boolean status;
    List<String> productSell_id;
}
