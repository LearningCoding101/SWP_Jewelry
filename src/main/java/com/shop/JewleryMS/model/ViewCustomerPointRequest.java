package com.shop.JewleryMS.Model;

import lombok.Data;
@Data
public class ViewCustomerPointRequest {
        long id;
        String phoneNumber;
        String email;
        int pointsAmount;
}
