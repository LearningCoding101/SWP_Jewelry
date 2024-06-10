package com.project.JewelryMS.model.OrderDetail;

import lombok.Data;

@Data
public class OrderPromotionRequest {
    Long productSell_ID;
    Integer quantity;
    Long PromotionID;
}
