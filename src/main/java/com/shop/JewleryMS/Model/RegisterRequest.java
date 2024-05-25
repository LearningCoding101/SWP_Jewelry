package com.shop.JewleryMS.Model;

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
