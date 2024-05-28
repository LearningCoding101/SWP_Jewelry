package com.shop.JewleryMS.model;

import lombok.Data;

@Data
public class ViewCustomerPointRequest {
        long id;
        String phoneNumber;
        String email;
        int pointAmount;
}
