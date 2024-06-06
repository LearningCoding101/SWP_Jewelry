package com.project.JewelryMS.model.Manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.JewelryMS.entity.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateManagerAccountRequest {
    String email;
    String aUsername;
    String aPassword;
    String accountName;
    Integer status;
    RoleEnum role;
}
