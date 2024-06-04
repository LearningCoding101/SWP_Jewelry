package com.project.JewelryMS.model.Manager;

import com.project.JewelryMS.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerAccountRequest {
    long user_ID;
    String email;
    String aUsername;
    String aPassword;
    String accountName;
    Integer status;
    RoleEnum role;
}
