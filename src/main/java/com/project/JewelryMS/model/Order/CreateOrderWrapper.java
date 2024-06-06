package com.project.JewelryMS.model.Order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderWrapper {
    private CreateOrderRequest orderRequest;
    private List<CreateOrderDetailRequest> detailList;
    private String email;
}
