package com.project.JewelryMS.model.Profile;

import com.project.JewelryMS.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManagerResponse {
    private String email;
    private String username;
    private String accountName;
}
