package com.shop.JewleryMS.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Account")
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_userID")
    int PK_userID;
    @Column(name = "email")
    String email;
    @Column(name = "aUsername")
    String aUsername;
    @Column(name = "aPassword")
    String aPassword;
    @Column(name = "accountName")
    String accountName;
}
