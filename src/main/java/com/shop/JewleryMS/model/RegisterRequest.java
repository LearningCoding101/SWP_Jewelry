package com.shop.JewleryMS.model;

import com.shop.JewleryMS.entity.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    String email;
    String aUsername;
    String aPassword;
    String accountName;
}
