package com.project.JewelryMS.model.Manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.entity.StaffAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerAccountResponse {
    int PK_userID;

    String email;

    String aUsername;

    String accountName;

    RoleEnum role;

}

