package com.project.JewelryMS.repository;

import com.project.JewelryMS.entity.Account;
import com.project.JewelryMS.entity.RoleEnum;
import com.project.JewelryMS.model.Manager.ManagerAccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerAccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT new com.project.JewelryMS.model.Manager.ManagerAccountResponse(a.PK_userID, a.email, a.aUsername, a.accountName, a.role) " +
            "FROM Account a " +
            "WHERE a.role = com.project.JewelryMS.entity.RoleEnum.ROLE_MANAGER AND a.status = 1")
    List<ManagerAccountResponse> findAllManagerAccounts();

    @Query("SELECT new com.project.JewelryMS.model.Manager.ManagerAccountResponse(a.PK_userID, a.email, a.aUsername, a.accountName, a.role) " +
            "FROM Account a " +
            "WHERE a.role = com.project.JewelryMS.entity.RoleEnum.ROLE_MANAGER AND a.PK_userID = :id AND a.status = 1")
    ManagerAccountResponse findManagerAccountById(Integer id);
}
