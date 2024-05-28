package com.shop.JewleryMS.model;

import com.shop.JewleryMS.entity.RoleEnum;
import lombok.Data;

@Data
public class AccountResponse {
    String username;
    String token;
    RoleEnum role;
}
