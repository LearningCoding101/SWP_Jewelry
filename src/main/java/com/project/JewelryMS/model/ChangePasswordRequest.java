package com.project.JewelryMS.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ChangePasswordRequest {
    private Long ID;
    private String oldPassword;
    private String newPassword;
}
