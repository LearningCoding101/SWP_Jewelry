package com.shop.JewleryMS.model;
import lombok.Data;

@Data
public class CreateCustomerRequest {//for Create methods, with the id autogenerated
    String email;
    String phoneNumber;
    int pointAmount;
}