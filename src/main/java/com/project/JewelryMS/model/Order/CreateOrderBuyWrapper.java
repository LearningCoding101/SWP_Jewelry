package com.project.JewelryMS.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderBuyWrapper {
    private CreateOrderRequest orderRequest;
    private List<CreateOrderBuyDetailRequest> BuyDetailList;
    private String email;


}
