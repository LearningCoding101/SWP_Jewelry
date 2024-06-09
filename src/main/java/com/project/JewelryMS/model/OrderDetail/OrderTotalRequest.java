package com.project.JewelryMS.model.OrderDetail;

import lombok.Data;

@Data
public class OrderTotalRequest {
    Float subTotal;
    Long PromotionID;
}
