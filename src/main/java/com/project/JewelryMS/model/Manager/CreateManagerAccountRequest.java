package com.project.JewelryMS.model.Manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateManagerAccountRequest {
    String email;
    String username;
    String aPassword;
    String accountName;
}
