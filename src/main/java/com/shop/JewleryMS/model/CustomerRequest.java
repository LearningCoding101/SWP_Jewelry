package com.shop.JewleryMS.Model;

import lombok.Data;

@Data
public class CustomerRequest {//For read methods, we pull all datas
    long id;
    String phoneNumber;
    String email;
    int pointsAmount;
}
